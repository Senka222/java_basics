import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Operation {

    private String accountType;             //  Тип счета
    private String accountNumber;           //  Номер счета
    private String currency;                //  Валюта
    private Date operationDay;              //  Дата оперции  // наверно LocalDate лучше?
    private String referenceWiring;         //  Референс проводки
    private String operationDescription;    //  Описание операции
    private BigDecimal income;              //  Приход
    private BigDecimal expense;             //  Расход

    // не слишком ли большой конструктор?
    public Operation(String accountType, String accountNumber, String currency, Date operationDay, String referenceWiring, String operationDescription, BigDecimal income, BigDecimal expense) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.operationDay = operationDay;
        this.referenceWiring = referenceWiring;
        this.operationDescription = operationDescription;
        this.income = income.setScale(2, RoundingMode.HALF_UP);
        this.expense = expense.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Описание операции: \"" + operationDescription + "\"" +
                "\n\tПриход: " + income + " " + currency +
                "\n\tРасход: " + expense + " " + currency +
                "\n\tДата операции: " + (new SimpleDateFormat("dd.MM.yy")).format(operationDay);
    }

    public BigDecimal getIncome() {
        return income;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public String getCurrency() {
        return currency;
    }
}
