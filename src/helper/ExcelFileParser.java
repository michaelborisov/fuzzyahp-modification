package helper;

import fuzzy.IntervalTypeTwoMF;
import fuzzy.TypeOneMF;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by michaelborisov on 26.03.17.
 */
public class ExcelFileParser {

    String pathToFile;

    public ExcelFileParser(String pathToFile){
        this.pathToFile = pathToFile;
    }

    public ArrayList<ArrayList<IntervalTypeTwoMF>> parseExcel() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(pathToFile));
        Workbook workbook = null;
        if (pathToFile.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (pathToFile.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
        int i = 0, j = 0;
        ArrayList<ArrayList<IntervalTypeTwoMF>> matrix = new ArrayList<ArrayList<IntervalTypeTwoMF>>();
        while (iterator.hasNext()) {
            matrix.add(new ArrayList<IntervalTypeTwoMF>());
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if(j < i){
                    try {
                        matrix.get(i).add(matrix.get(j).get(i). getReciprocal());
                        j++;
                        continue;
                    }catch (IndexOutOfBoundsException iobEx){
                        //iobEx.printStackTrace();
                    }

                }
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        matrix.get(i).add(convertStringToIntervalTypeTwoMF(cell.getStringCellValue()));
                        //System.out.print(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        System.out.print(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue());
                        break;

                }
                j++;
            }
            i++;
            j = 0;
        }
        matrix.remove(matrix.size() - 1);
        workbook.close();
        inputStream.close();
        return matrix;
    }

    public IntervalTypeTwoMF convertStringToIntervalTypeTwoMF(String stringRepresentation){
        String[] parts = stringRepresentation.split(";");

        String lowerBounds = parts[0].replace("(", "").replace(")", "");
        String middle = parts[1].replace("(", "").replace(")", "");
        String upperBounds = parts[2].replace("(", "").replace(")", "");

        double shift = 1.0;
        Random randomGenerator = new Random();
        int nextRand = randomGenerator.nextInt(100);
        if (nextRand % 5 == 0) {
            shift = 1.0;
        }else if(nextRand % 3 == 0) {
            shift = 1 / 1.7;
        } else{
            shift = 1.4;
        }


        String[] partsLowerBounds = lowerBounds.split(",");
        Double lowerBoundUpperMF = Double.valueOf(partsLowerBounds[0]);
        Double lowerBoundLowerMF = Double.valueOf(partsLowerBounds[1]);


        Double middleValue = Double.valueOf(middle);

        String[] partsUpperBounds = upperBounds.split(",");
        Double upperBoundUpperMF = Double.valueOf(partsUpperBounds[1]);
        Double upperBoundLowerMF = Double.valueOf(partsUpperBounds[0]);

        TypeOneMF lowerMF = new TypeOneMF(lowerBoundLowerMF, middleValue, upperBoundLowerMF);
        TypeOneMF upperMF = new TypeOneMF(lowerBoundUpperMF, middleValue, upperBoundUpperMF);

        IntervalTypeTwoMF iTypeTwo = new IntervalTypeTwoMF(lowerMF, upperMF);
        return iTypeTwo;
    }
}
