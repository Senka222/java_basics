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

        /*
        BigDecimal incomeSum = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expenseSum = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

        for (Operation operation : operations) {
            incomeSum = incomeSum.add(operation.getIncome());
            expenseSum = expenseSum.add(operation.getExpense());
        }

        System.out.println("Общий доход: \t" + incomeSum
                + "\nОбщий расход: \t" + expenseSum);
        */

        BigDecimal incomeSum = operations.stream()
                .map(Operation::getIncome)
                .reduce(BigDecimal::add)
                .get()
                .setScale(2, RoundingMode.HALF_UP); // нужно ли проверять на isPresent?
        System.out.println("Сумма доходов: " + incomeSum);

        BigDecimal expenseSum = operations.stream()
                .map(Operation::getExpense)
                .reduce(BigDecimal::add)
                .get()
                .setScale(2, RoundingMode.HALF_UP); // нужно ли проверять на isPresent?
        System.out.println("Сумма расходов: " + expenseSum);


        // Разбивка по расходу
        System.out.println("==========================\n" +
                "Групировка расходов:");
        operations.stream()
                .filter(operation -> operation.getExpense().compareTo(BigDecimal.ZERO) > 0)
                .collect(
                        Collectors.groupingBy(Operation::getOperationDescription,
                                Collectors.mapping(Operation::getExpense,
                                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .forEach((description, sum) -> System.out.println(description + " : \t"  + sum));

        // Разбивка по доходу
        System.out.println("==========================\n" +
                "Групировка доходов:");
        operations.stream()
                .filter(operation -> operation.getIncome().compareTo(BigDecimal.ZERO) > 0)
                .collect(
                        Collectors.groupingBy(Operation::getOperationDescription,
                                Collectors.mapping(Operation::getIncome,
                                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                        ))
                .forEach((description, sum) -> System.out.println(description + " : \t"  + sum));


        /*

        operations.stream()
                .collect(
                        Collectors.groupingBy(Operation::getOperationDescription,
                                Collectors.mapping(Summary::fromOperation,
                                        Collectors.reducing(BigDecimal.ZERO, Summary::merge) //  вообще не понимаю что здесь принимает
                )))
                .forEach((description, sum) -> System.out.println(description + ":\t" + sum.));

    }

    private static class Summary {
        BigDecimal income;
        BigDecimal expense;

        Summary(BigDecimal income, BigDecimal expense) {
            this.income = income.setScale(2, RoundingMode.HALF_UP);
            this.expense = expense.setScale(2, RoundingMode.HALF_UP);
        }

        static Summary merge(Summary s1, Summary s2) {
            return new Summary(s1.income.add(s2.income), s1.expense.add(s2.expense));
        }

        static Summary fromOperation(Operation o) {
            return new Summary(o.getIncome(), o.getExpense());
        }


         */
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
                        splitDescription(fragments[5]),
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
    /*
    правильно ли было возвращать массив? я делаю это из соображения
    что нам вдруг могут понадобиться остальные части
    да и поля оставлял по той же причине, но по-чикаем как говорится

    private static String[] splitDescription (String fragment){

        return fragment.split("\\s{4}");
    }
    */

    private static String splitDescription (String description){
        String[] fragments = description.split("\\s{4}");
        fragments = fragments[1].split("/|\\\\");
        return fragments[fragments.length - 1].trim();
    }

    private static BigDecimal stringToBigDecimal (String fragment){
        return BigDecimal.valueOf(Double.parseDouble(fragment.replaceAll("\"", "")
                .replaceAll(",", ".")));
    }

    private static LocalDate stringToLocalDate (String fragment){
        return LocalDate.parse(fragment, dateFormat);
    }
}
