package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import java.util.List;
import java.io.PrintStream;

public class Main {
   public static void main(String[] args) {
      ClassSchedule schedule = new ClassSchedule();

      try {
         
         List<String[]> csvOriginal = schedule.getCsv();
         JsonObject jsonOriginal = schedule.getJson();
         PrintStream var10000 = System.out;
         int var10001 = csvOriginal.size();
         var10000.println("Sections Found (CSV): " + (var10001 - 1));
         JsonArray sections = (JsonArray)jsonOriginal.get("section");
         System.out.println("Sections Found (JSON): " + sections.size());
         
         String jsonConverted = schedule.convertCsvToJsonString(csvOriginal);
         System.out.println("\nCSV to JSON Conversion: ");
         System.out.println(jsonConverted);
         
         String csvConverted = schedule.convertJsonToCsvString(jsonOriginal);
         System.out.println("\nJSON to CSV Conversion: ");
         System.out.println(csvConverted);
         
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
 




