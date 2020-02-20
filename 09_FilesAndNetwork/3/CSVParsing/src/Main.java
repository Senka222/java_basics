import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.format.DateTimeFormatter;

public class Main {

    private static String fileForParsing = "res/movementList.csv";
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yy");

    public static void main(String[] args) {

        ArrayList<Operation> operations = parsingFromFile();

        BigDecimal incomeSum = operations.stream()
                .map(Operation::getIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println("Сумма доходов: " + incomeSum);

        BigDecimal expenseSum = operations.stream()
                .map(Operation::getExpense)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);    // тогда вопрос, а где тогда лучше округлять, здесь нормально
        System.out.println("Сумма расходов: " + expenseSum);    // или лучше здесь перед самым выводом

        System.out.println("==========================\n" +
                "Групировка расходов и доходов:\n" +
                "Описание: \tДоход\tРасход");
        operations.stream()
                .collect(
                        Collectors.groupingBy(Operation::getOperationDescription,
                                Collectors.mapping(Summary::fromOperation,
                                        Collectors.reducing(new Summary(BigDecimal.ZERO, BigDecimal.ZERO), Summary::merge)
                )))
                .forEach((description, sum) -> System.out.println(description + ": \t" + sum.income + "\t" + sum.expense));

    }

    private static class Summary {
        BigDecimal income;
        BigDecimal expense;

        Summary(BigDecimal income, BigDecimal expense) {
            this.income = income.setScale(2, RoundingMode.HALF_UP);     // здесь вопрос аналогичный
            this.expense = expense.setScale(2, RoundingMode.HALF_UP);
        }

        static Summary merge(Summary s1, Summary s2) {
            return new Summary(s1.income.add(s2.income), s1.expense.add(s2.expense));
        }

        static Summary fromOperation(Operation o) {
            return new Summary(o.getIncome(), o.getExpense());
        }
    }


    private static ArrayList<Operation> parsingFromFile(){

        ArrayList<Operation> operations = new ArrayList<>();
        try{
            Stream<String> lines = Files.lines(Paths.get(fileForParsing));

            for (String line : (Iterable<String>) lines.skip(1)::iterator){
                String[] fragments = line.split(",", 8);
                if (fragments.length != 8){
                    System.out.println("Ошибка в строке: " + line);
                    continue;
                }
                operations.add(new Operation(
                        stringToLocalDate(fragments[3]),
                        cleanDescription(fragments[5]),
                        stringToBigDecimal(fragments[6]),
                        stringToBigDecimal(fragments[7])
                ));
            }
        }
        catch(IOException ex){
            System.out.println("Файл не найден!");
            ex.printStackTrace();
        }
        return operations;
    }

    private static String cleanDescription (String description){
        String[] fragments = description.split("\\s{4}");
        fragments = fragments[1].split("/|\\\\");
        return fragments[fragments.length - 1].trim();
    }

    private static BigDecimal stringToBigDecimal (String fragment){
        return new BigDecimal(fragment.replaceAll("\"", "").replaceAll(",", "."));
    }

    private static LocalDate stringToLocalDate (String fragment){
        return LocalDate.parse(fragment, dateFormat);
    }
}
