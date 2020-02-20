import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Operation {

    private LocalDate operationDay;
    private String operationDescription;
    private BigDecimal income;
    private BigDecimal expense;

    public Operation(LocalDate operationDay, String operationDescription, BigDecimal income, BigDecimal expense) {

        this.operationDay = operationDay;
        this.operationDescription = operationDescription;
        this.income = income;
        this.expense = expense;
    }

    @Override
    public String toString() {
        return "Описание операции: \"" + operationDescription + "\"" +
                "\n\tПриход: " + income + " RUR" +
                "\n\tРасход: " + expense + " RUR" +
                "\n\tДата операции: " + operationDay.format(DateTimeFormatter.ofPattern("dd.MM.yy"));
    }

    public BigDecimal getIncome() {
        return income;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public String getOperationDescription(){
        return operationDescription;
    }
}

