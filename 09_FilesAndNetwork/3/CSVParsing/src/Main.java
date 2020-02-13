import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Main {

    private static String fileForParsing = "res/movementList.csv";
    private static String dateFormat = "dd.MM.yy";

    public static void main(String[] args) {

        ArrayList<Operation> operations = parsingFromFile();

        BigDecimal incomeSum = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expenseSum = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

        for (Operation operation : operations){
            if (operation.getExpense().compareTo(BigDecimal.valueOf(0)) > 0) {
                System.out.println(operation);
            }
            incomeSum = incomeSum.add(operation.getExpense());
            expenseSum = expenseSum.add(operation.getExpense());
        }

        System.out.println("Общий доход: " + incomeSum
                + "\nОбщий расход: " + expenseSum);

    }
    private static ArrayList<Operation> parsingFromFile(){

        ArrayList<Operation> operations = new ArrayList<>(); // или лучше мапу, но что тогда задавать ключем? референс проводки?
        try{
            Stream<String> lines = Files.lines(Paths.get(fileForParsing));

            for (String line : (Iterable<String>) lines.skip(1)::iterator){  // так я сделал чтобы пропустить первую строку, но что делает итератор моя не понимать
                String[] fragments = line.split(",", 8);
                if (fragments.length != 8){
                    System.out.println("Ошибка в строке: " + line);
                    continue;
                }
                try {
                    operations.add(new Operation(fragments[0], fragments[1], fragments[2],
                            (new SimpleDateFormat(dateFormat)).parse(fragments[3]),
                            fragments[4], splitDescription(fragments[5])[1],
                            BigDecimal.valueOf(Double.parseDouble(fragments[6])),
                            BigDecimal.valueOf(Double.parseDouble(fragments[7].replaceAll("\"", "")
                                    .replaceAll(",", ".")))
                    ));
                }
                catch (ParseException ex){
                    ex.printStackTrace();
                }
            }
        }
        catch(IOException ex){
            System.out.println("Файл не найден!");
            ex.printStackTrace();
        }
        return operations;
    }

    private static String[] splitDescription (String fragment){
        return fragment.split("\\s{4}");
    }
}
