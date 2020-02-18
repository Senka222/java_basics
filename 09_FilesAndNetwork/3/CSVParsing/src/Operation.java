import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Operation {

    private final String currency = "RUR";  //  Валюта // раз у нас она везде одна и та же, наверно можно и константу
                                                       // но идея хочет запихнуть её в toString
    private LocalDate operationDay;         //  Дата оперции
    private String operationDescription;    //  Описание операции
    private BigDecimal income;              //  Приход
    private BigDecimal expense;             //  Расход

    public Operation(LocalDate operationDay, String operationDescription, BigDecimal income, BigDecimal expense) {

        this.operationDay = operationDay;
        this.operationDescription = operationDescription;
        this.income = income.setScale(2, RoundingMode.HALF_UP);
        this.expense = expense.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Описание операции: \"" + operationDescription + "\"" +
                "\n\tПриход: " + income + " " + currency +
                "\n\tРасход: " + expense + " " + currency +
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

