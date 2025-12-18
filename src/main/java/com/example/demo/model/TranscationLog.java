@Entity
public class TransactionLog {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

    private Double amount;

    private String description;

    private LocalDate transactionDate;
}
