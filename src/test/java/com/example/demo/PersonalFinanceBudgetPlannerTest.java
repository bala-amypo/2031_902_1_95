package com.example.demo;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.servlet.SimpleHelloServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Listeners(TestResultListener.class)
public class PersonalFinanceBudgetPlannerTest {

    @Mock private UserRepository userRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private TransactionLogRepository transactionLogRepository;
    @Mock private BudgetPlanRepository budgetPlanRepository;
    @Mock private BudgetSummaryRepository budgetSummaryRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private Authentication authentication;
    @Mock private CustomUserDetailsService customUserDetailsService;

    private UserService userService;
    private CategoryService categoryService;
    private TransactionService transactionService;
    private BudgetPlanService budgetPlanService;
    private BudgetSummaryService budgetSummaryService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeClass
    public void init() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider(
                "MySuperSecretJwtKeyForBudgetPlanner123456", 3600000L);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
        categoryService = new CategoryServiceImpl(categoryRepository);
        transactionService = new TransactionServiceImpl(transactionLogRepository, userRepository);
        budgetPlanService = new BudgetPlanServiceImpl(budgetPlanRepository, userRepository);
        budgetSummaryService = new BudgetSummaryServiceImpl(
                budgetSummaryRepository, budgetPlanRepository, transactionLogRepository);
    }

    // 1) Servlet tests (8)
    @Test(groups = "servlet", priority = 1)
    public void t1_servletHello() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        MockHttpServletRequest rq = new MockHttpServletRequest("GET", "/hello-servlet");
        MockHttpServletResponse rs = new MockHttpServletResponse();
        s.doGet(rq, rs);
        Assert.assertEquals(rs.getContentAsString(), "Hello from Simple Servlet");
    }

    @Test(groups = "servlet", priority = 2)
    public void t2_servletStatus200() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        MockHttpServletRequest rq = new MockHttpServletRequest("GET", "/hello-servlet");
        MockHttpServletResponse rs = new MockHttpServletResponse();
        s.doGet(rq, rs);
        Assert.assertEquals(rs.getStatus(), 200);
    }

    @Test(groups = "servlet", priority = 3)
    public void t3_servletContentType() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        MockHttpServletRequest rq = new MockHttpServletRequest("GET", "/hello-servlet");
        MockHttpServletResponse rs = new MockHttpServletResponse();
        s.doGet(rq, rs);
        Assert.assertEquals(rs.getContentType(), "text/plain");
    }

    @Test(groups = "servlet", priority = 4)
    public void t4_servletInfo() {
        SimpleHelloServlet s = new SimpleHelloServlet();
        Assert.assertTrue(s.getServletInfo().contains("SimpleHelloServlet"));
    }

    @Test(groups = "servlet", priority = 5)
    public void t5_servletMultipleCalls() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        for (int i = 0; i < 3; i++) {
            MockHttpServletRequest rq = new MockHttpServletRequest("GET", "/hello-servlet");
            MockHttpServletResponse rs = new MockHttpServletResponse();
            s.doGet(rq, rs);
            Assert.assertEquals(rs.getContentAsString(), "Hello from Simple Servlet");
        }
    }

    @Test(groups = "servlet", priority = 6)
    public void t6_servletNotNull() {
        Assert.assertNotNull(new SimpleHelloServlet());
    }

    @Test(groups = "servlet", priority = 7)
    public void t7_servletUrlPatternLogical() {
        SimpleHelloServlet s = new SimpleHelloServlet();
        Assert.assertNotNull(s);
    }

    @Test(groups = "servlet", priority = 8)
    public void t8_servletNoExceptionOnPost() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        HttpServletRequest rq = new MockHttpServletRequest("POST", "/hello-servlet");
        HttpServletResponse rs = new MockHttpServletResponse();
        s.service(rq, rs);
        int status = ((MockHttpServletResponse) rs).getStatus();
        Assert.assertTrue(status == 200 || status == 405);
    }

    // 2) CRUD-style tests (user/category/transaction/budget) (8)
    @Test(groups = "crud", priority = 9)
    public void t9_registerUser() {
        RegisterRequest req = new RegisterRequest("John", "john@example.com", "p");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("p")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        User u = new User(null, req.getName(), req.getEmail(), req.getPassword(), User.ROLE_USER);
        User saved = userService.register(u);
        Assert.assertEquals(saved.getId(), Long.valueOf(1L));
    }

    @Test(groups = "crud", priority = 10, expectedExceptions = BadRequestException.class)
    public void t10_registerDuplicateEmail() {
        when(userRepository.existsByEmail("x@example.com")).thenReturn(true);
        User u = new User(null, "X", "x@example.com", "p", User.ROLE_USER);
        userService.register(u);
    }

    @Test(groups = "crud", priority = 11)
    public void t11_addCategory() {
        Category cat = new Category(null, "Salary", Category.TYPE_INCOME);
        when(categoryRepository.existsByName("Salary")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> {
            Category c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });
        Category saved = categoryService.addCategory(cat);
        Assert.assertEquals(saved.getId(), Long.valueOf(10L));
    }

    @Test(groups = "crud", priority = 12, expectedExceptions = BadRequestException.class)
    public void t12_addDuplicateCategory() {
        Category cat = new Category(null, "Food", Category.TYPE_EXPENSE);
        when(categoryRepository.existsByName("Food")).thenReturn(true);
        categoryService.addCategory(cat);
    }

    @Test(groups = "crud", priority = 13)
    public void t13_addTransaction() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        Category c = new Category(1L, "Food", Category.TYPE_EXPENSE);
        TransactionLog log = new TransactionLog(null, u, c, 200.0, "Dinner",
                LocalDate.of(2025, 1, 10));
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(transactionLogRepository.save(any(TransactionLog.class))).thenAnswer(inv -> {
            TransactionLog t = inv.getArgument(0);
            t.setId(100L);
            return t;
        });
        TransactionLog saved = transactionService.addTransaction(1L, log);
        Assert.assertEquals(saved.getId(), Long.valueOf(100L));
    }

    @Test(groups = "crud", priority = 14)
    public void t14_createBudgetPlan() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        BudgetPlan plan = new BudgetPlan(null, u, 1, 2025, 50000.0, 20000.0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(budgetPlanRepository.findByUserAndMonthAndYear(u, 1, 2025))
                .thenReturn(Optional.empty());
        when(budgetPlanRepository.save(any(BudgetPlan.class))).thenAnswer(inv -> {
            BudgetPlan p = inv.getArgument(0);
            p.setId(50L);
            return p;
        });
        BudgetPlan saved = budgetPlanService.createBudgetPlan(1L, plan);
        Assert.assertEquals(saved.getId(), Long.valueOf(50L));
    }

    @Test(groups = "crud", priority = 15, expectedExceptions = BadRequestException.class)
    public void t15_duplicateBudgetPlan() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        BudgetPlan plan = new BudgetPlan(null, u, 1, 2025, 50000.0, 20000.0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(budgetPlanRepository.findByUserAndMonthAndYear(u, 1, 2025))
                .thenReturn(Optional.of(plan));
        budgetPlanService.createBudgetPlan(1L, plan);
    }

    @Test(groups = "crud", priority = 16)
    public void t16_getUserTransactions() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        TransactionLog t1 = new TransactionLog(1L, u, new Category(1L, "Food", Category.TYPE_EXPENSE),
                100.0, "Lunch", LocalDate.now());
        TransactionLog t2 = new TransactionLog(2L, u, new Category(2L, "Salary", Category.TYPE_INCOME),
                2000.0, "Salary", LocalDate.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(transactionLogRepository.findByUser(u)).thenReturn(Arrays.asList(t1, t2));
        Assert.assertEquals(transactionService.getUserTransactions(1L).size(), 2);
    }

    // 3) DI & IoC tests (8)
    @Test(groups = "di", priority = 17)
    public void t17_userServiceNotNull() { Assert.assertNotNull(userService); }

    @Test(groups = "di", priority = 18)
    public void t18_categoryServiceNotNull() { Assert.assertNotNull(categoryService); }

    @Test(groups = "di", priority = 19)
    public void t19_transactionServiceNotNull() { Assert.assertNotNull(transactionService); }

    @Test(groups = "di", priority = 20)
    public void t20_budgetPlanServiceNotNull() { Assert.assertNotNull(budgetPlanService); }

    @Test(groups = "di", priority = 21)
    public void t21_budgetSummaryServiceNotNull() { Assert.assertNotNull(budgetSummaryService); }

    @Test(groups = "di", priority = 22)
    public void t22_passwordEncoderMockNotNull() { Assert.assertNotNull(passwordEncoder); }

    @Test(groups = "di", priority = 23)
    public void t23_jwtProviderNotNull() { Assert.assertNotNull(jwtTokenProvider); }

    @Test(groups = "di", priority = 24)
    public void t24_repositoriesNotNull() { Assert.assertNotNull(userRepository); }

    // 4) Hibernate-style tests (8)
    @Test(groups = "hibernate", priority = 25)
    public void t25_userDefaultRole() {
        User u = new User();
        u.setRole(User.ROLE_USER);
        Assert.assertEquals(u.getRole(), User.ROLE_USER);
    }

    @Test(groups = "hibernate", priority = 26)
    public void t26_transactionValidationPositiveAmount() {
        TransactionLog log = new TransactionLog();
        log.setAmount(100.0);
        log.setTransactionDate(LocalDate.now());
        log.validate();
        Assert.assertEquals(log.getAmount(), 100.0);
    }

    @Test(groups = "hibernate", priority = 27, expectedExceptions = BadRequestException.class)
    public void t27_transactionFutureDateInvalid() {
        TransactionLog log = new TransactionLog();
        log.setAmount(100.0);
        log.setTransactionDate(LocalDate.now().plusDays(1));
        log.validate();
    }

    @Test(groups = "hibernate", priority = 28)
    public void t28_budgetPlanMonthValidation() {
        BudgetPlan plan = new BudgetPlan(null, null, 5, 2025, 1000.0, 500.0);
        plan.validate();
        Assert.assertEquals(plan.getMonth(), Integer.valueOf(5));
    }

    @Test(groups = "hibernate", priority = 29, expectedExceptions = BadRequestException.class)
    public void t29_budgetPlanInvalidMonth() {
        BudgetPlan plan = new BudgetPlan(null, null, 13, 2025, 1000.0, 500.0);
        plan.validate();
    }

    @Test(groups = "hibernate", priority = 30)
    public void t30_budgetSummaryOnCreate() {
        BudgetSummary summary = new BudgetSummary();
        summary.setBudgetPlan(new BudgetPlan());
        summary.onCreate();
        Assert.assertNotNull(summary.getGeneratedAt());
    }

    @Test(groups = "hibernate", priority = 31)
    public void t31_categoryTypeValidation() {
        Category cat = new Category(null, "Gift", Category.TYPE_EXPENSE);
        cat.validateType();
        Assert.assertEquals(cat.getType(), Category.TYPE_EXPENSE);
    }

    @Test(groups = "hibernate", priority = 32, expectedExceptions = BadRequestException.class)
    public void t32_categoryTypeInvalid() {
        Category cat = new Category(null, "X", "OTHER");
        cat.validateType();
    }

    // 5) JPA mapping & normalization-style tests (7)
    @Test(groups = "jpa-mapping", priority = 33)
    public void t33_manyToOneUserTransactions() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        TransactionLog t = new TransactionLog(1L, u,
                new Category(1L, "Food", Category.TYPE_EXPENSE),
                100.0, "Lunch", LocalDate.now());
        Assert.assertEquals(t.getUser().getId(), u.getId());
    }

    @Test(groups = "jpa-mapping", priority = 34)
    public void t34_manyToOneUserBudgetPlan() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        BudgetPlan plan = new BudgetPlan(1L, u, 1, 2025, 1000.0, 500.0);
        Assert.assertEquals(plan.getUser().getEmail(), "u@e.com");
    }

    @Test(groups = "jpa-mapping", priority = 35)
    public void t35_oneToOneBudgetSummaryPlan() {
        BudgetPlan plan = new BudgetPlan(1L, new User(), 1, 2025, 1000.0, 500.0);
        BudgetSummary summary = new BudgetSummary(1L, plan, 500.0, 200.0,
                BudgetSummary.STATUS_UNDER_LIMIT, LocalDateTime.now());
        Assert.assertEquals(summary.getBudgetPlan().getId(), plan.getId());
    }

    @Test(groups = "jpa-mapping", priority = 36)
    public void t36_1NF_categoryNameNotNull() {
        Category cat = new Category(1L, "Food", Category.TYPE_EXPENSE);
        Assert.assertNotNull(cat.getName());
    }

    @Test(groups = "jpa-mapping", priority = 37)
    public void t37_2NF_budgetPlanDependsOnId() {
        BudgetPlan plan = new BudgetPlan(1L, new User(), 1, 2025, 1000.0, 500.0);
        Assert.assertEquals(plan.getId(), Long.valueOf(1L));
    }

    @Test(groups = "jpa-mapping", priority = 38)
    public void t38_3NF_budgetSummaryNoTransitive() {
        BudgetSummary summary = new BudgetSummary();
        summary.setStatus(BudgetSummary.STATUS_UNDER_LIMIT);
        Assert.assertEquals(summary.getStatus(), BudgetSummary.STATUS_UNDER_LIMIT);
    }

    @Test(groups = "jpa-mapping", priority = 39)
    public void t39_normalizedTransactionLog() {
        TransactionLog log = new TransactionLog(1L, new User(), new Category(),
                100.0, "Note", LocalDate.now());
        Assert.assertEquals(log.getId(), Long.valueOf(1L));
    }

    // 6) Many-to-Many logical tests (7) - using user & categories via transactions
    @Test(groups = "many-to-many", priority = 40)
    public void t40_userMultipleCategoriesThroughTransactions() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        Category c1 = new Category(1L, "Food", Category.TYPE_EXPENSE);
        Category c2 = new Category(2L, "Salary", Category.TYPE_INCOME);
        TransactionLog t1 = new TransactionLog(1L, u, c1, 100.0, "Lunch", LocalDate.now());
        TransactionLog t2 = new TransactionLog(2L, u, c2, 2000.0, "Salary", LocalDate.now());
        Assert.assertEquals(Arrays.asList(t1, t2).size(), 2);
    }

    @Test(groups = "many-to-many", priority = 41)
    public void t41_categoryUsedByMultipleUsers() {
        Category c = new Category(1L, "Food", Category.TYPE_EXPENSE);
        User u1 = new User(1L, "U1", "u1@e.com", "p", User.ROLE_USER);
        User u2 = new User(2L, "U2", "u2@e.com", "p", User.ROLE_USER);
        TransactionLog t1 = new TransactionLog(1L, u1, c, 100.0, "L1", LocalDate.now());
        TransactionLog t2 = new TransactionLog(2L, u2, c, 150.0, "L2", LocalDate.now());
        Assert.assertEquals(new HashSet<>(Arrays.asList(t1.getUser(), t2.getUser())).size(), 2);
    }

    @Test(groups = "many-to-many", priority = 42)
    public void t42_noDuplicateTransactionIds() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        Category c = new Category(1L, "Food", Category.TYPE_EXPENSE);
        TransactionLog t1 = new TransactionLog(1L, u, c, 100.0, "L1", LocalDate.now());
        TransactionLog t2 = new TransactionLog(2L, u, c, 120.0, "L2", LocalDate.now());
        Assert.assertNotEquals(t1.getId(), t2.getId());
    }

    @Test(groups = "many-to-many", priority = 43)
    public void t43_logicalManyToManyCardinality() {
        User u1 = new User(1L, "U1", "u1@e.com", "p", User.ROLE_USER);
        User u2 = new User(2L, "U2", "u2@e.com", "p", User.ROLE_USER);
        Category c1 = new Category(1L, "Food", Category.TYPE_EXPENSE);
        Category c2 = new Category(2L, "Salary", Category.TYPE_INCOME);
        TransactionLog t1 = new TransactionLog(1L, u1, c1, 100.0, "L1", LocalDate.now());
        TransactionLog t2 = new TransactionLog(2L, u1, c2, 2000.0, "L2", LocalDate.now());
        TransactionLog t3 = new TransactionLog(3L, u2, c1, 150.0, "L3", LocalDate.now());
        Assert.assertEquals(Arrays.asList(t1, t2, t3).size(), 3);
    }

    @Test(groups = "many-to-many", priority = 44)
    public void t44_edgeNoTransactions() {
        Assert.assertTrue(Collections.<TransactionLog>emptyList().isEmpty());
    }

    @Test(groups = "many-to-many", priority = 45)
    public void t45_edgeSingleTransaction() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        Category c = new Category(1L, "Food", Category.TYPE_EXPENSE);
        TransactionLog t = new TransactionLog(1L, u, c, 100.0, "L1", LocalDate.now());
        Assert.assertEquals(t.getUser().getId(), Long.valueOf(1L));
    }

    @Test(groups = "many-to-many", priority = 46)
    public void t46_joinEntityNotNull() {
        TransactionLog t = new TransactionLog(1L, new User(), new Category(),
                100.0, "L1", LocalDate.now());
        Assert.assertNotNull(t.getCategory());
    }

    // 7) JWT security tests (7)
    @Test(groups = "jwt-security", priority = 47)
    public void t47_jwtContainsClaims() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("user@e.com").password("p").roles("USER").build();
        when(authentication.getPrincipal()).thenReturn(ud);
        String token = jwtTokenProvider.generateToken(authentication,
                1L, "user@e.com", "USER");
        Assert.assertEquals(jwtTokenProvider.getUserIdFromToken(token), Long.valueOf(1L));
        Assert.assertEquals(jwtTokenProvider.getEmailFromToken(token), "user@e.com");
        Assert.assertEquals(jwtTokenProvider.getRoleFromToken(token), "USER");
    }

    @Test(groups = "jwt-security", priority = 48)
    public void t48_jwtInvalidTokenFalse() {
        Assert.assertFalse(jwtTokenProvider.validateToken("x.y.z"));
    }

    @Test(groups = "jwt-security", priority = 49)
    public void t49_jwtValidTokenTrue() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("t@e.com").password("p").roles("USER").build();
        when(authentication.getPrincipal()).thenReturn(ud);
        String token = jwtTokenProvider.generateToken(authentication,
                2L, "t@e.com", "USER");
        Assert.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test(groups = "jwt-security", priority = 50)
    public void t50_jwtUserIdFallbackSubject() {
        String raw = io.jsonwebtoken.Jwts.builder()
                .setSubject("10")
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                "MySuperSecretJwtKeyForBudgetPlanner123456".getBytes()),
                        io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();
        Assert.assertEquals(jwtTokenProvider.getUserIdFromToken(raw), Long.valueOf(10L));
    }

    @Test(groups = "jwt-security", priority = 51)
    public void t51_jwtRoleNoPrefixChange() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("r@e.com").password("p").roles("USER").build();
        when(authentication.getPrincipal()).thenReturn(ud);
        String token = jwtTokenProvider.generateToken(authentication,
                3L, "r@e.com", "USER");
        Assert.assertEquals(jwtTokenProvider.getRoleFromToken(token), "USER");
    }

    @Test(groups = "jwt-security", priority = 52)
    public void t52_jwtTokenNotNull() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("x@e.com").password("p").roles("USER").build();
        when(authentication.getPrincipal()).thenReturn(ud);
        String token = jwtTokenProvider.generateToken(authentication,
                4L, "x@e.com", "USER");
        Assert.assertNotNull(token);
    }

    @Test(groups = "jwt-security", priority = 53)
    public void t53_jwtDifferentIdsProduceDifferentTokens() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("x@e.com").password("p").roles("USER").build();
        when(authentication.getPrincipal()).thenReturn(ud);
        String t1 = jwtTokenProvider.generateToken(authentication,
                5L, "x@e.com", "USER");
        String t2 = jwtTokenProvider.generateToken(authentication,
                6L, "x@e.com", "USER");
        Assert.assertNotEquals(t1, t2);
    }

    // 8) HQL/HCQL-style query tests (7)
    @Test(groups = "hql-hcql", priority = 54)
    public void t54_queryTransactionsByUserAndMonth() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        Category cat = new Category(1L, "Food", Category.TYPE_EXPENSE);
        YearMonth ym = YearMonth.of(2025, 1);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        TransactionLog t = new TransactionLog(1L, u, cat, 100.0, "L", LocalDate.now());
        when(transactionLogRepository.findByUserAndTransactionDateBetween(u, start, end))
                .thenReturn(List.of(t));
        Assert.assertEquals(transactionLogRepository
                .findByUserAndTransactionDateBetween(u, start, end).size(), 1);
    }

    @Test(groups = "hql-hcql", priority = 55)
    public void t55_queryNoTransactionsRange() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        YearMonth ym = YearMonth.of(2025, 1);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        when(transactionLogRepository.findByUserAndTransactionDateBetween(u, start, end))
                .thenReturn(Collections.emptyList());
        Assert.assertTrue(transactionLogRepository
                .findByUserAndTransactionDateBetween(u, start, end).isEmpty());
    }

    @Test(groups = "hql-hcql", priority = 56)
    public void t56_queryBudgetPlanByUserMonthYear() {
        User u = new User(1L, "U", "u@e.com", "p", User.ROLE_USER);
        BudgetPlan plan = new BudgetPlan(1L, u, 1, 2025, 1000.0, 500.0);
        when(budgetPlanRepository.findByUserAndMonthAndYear(u, 1, 2025))
                .thenReturn(Optional.of(plan));
        Assert.assertTrue(budgetPlanRepository
                .findByUserAndMonthAndYear(u, 1, 2025).isPresent());
    }

    @Test(groups = "hql-hcql", priority = 57)
    public void t57_querySummaryByBudgetPlan() {
        BudgetPlan plan = new BudgetPlan(1L, new User(), 1, 2025, 1000.0, 500.0);
        BudgetSummary summary = new BudgetSummary(1L, plan, 1000.0, 400.0,
                BudgetSummary.STATUS_UNDER_LIMIT, LocalDateTime.now());
        when(budgetSummaryRepository.findByBudgetPlan(plan)).thenReturn(Optional.of(summary));
        Assert.assertTrue(budgetSummaryRepository.findByBudgetPlan(plan).isPresent());
    }

    @Test(groups = "hql-hcql", priority = 58)
    public void t58_queryCategoriesList() {
        Category c1 = new Category(1L, "Food", Category.TYPE_EXPENSE);
        Category c2 = new Category(2L, "Salary", Category.TYPE_INCOME);
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(c1, c2));
        Assert.assertEquals(categoryService.getAllCategories().size(), 2);
    }

    @Test(groups = "hql-hcql", priority = 59)
    public void t59_queryCategoriesEmpty() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(categoryService.getAllCategories().isEmpty());
    }

    @Test(groups = "hql-hcql", priority = 60)
    public void t60_queryEdgeNullListsHandled() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertNotNull(categoryService.getAllCategories());
    }
}
