package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Arrays;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ClassSchedule {
   private final String CSV_FILENAME = "jsu_sp24_v1.csv";
   private final String JSON_FILENAME = "jsu_sp24_v1.json";
   private final String CRN_COL_HEADER = "crn";
   private final String SUBJECT_COL_HEADER = "subject";
   private final String NUM_COL_HEADER = "num";
   private final String DESCRIPTION_COL_HEADER = "description";
   private final String SECTION_COL_HEADER = "section";
   private final String TYPE_COL_HEADER = "type";
   private final String CREDITS_COL_HEADER = "credits";
   private final String START_COL_HEADER = "start";
   private final String END_COL_HEADER = "end";
   private final String DAYS_COL_HEADER = "days";
   private final String WHERE_COL_HEADER = "where";
   private final String SCHEDULE_COL_HEADER = "schedule";
   private final String INSTRUCTOR_COL_HEADER = "instructor";
   private final String SUBJECTID_COL_HEADER = "subjectid";

   public String convertCsvToJsonString(List<String[]> csv) {
      JsonObject json = new JsonObject();
      JsonObject scheduletypeMap = new JsonObject();
      JsonObject subjectMap = new JsonObject();
      JsonObject courseMap = new JsonObject();
      JsonArray sectionList = new JsonArray();
      Iterator<String[]> iterator = csv.iterator();
      String[] headers = (String[])iterator.next();
      HashMap<String, Integer> headerList = new HashMap();

      for(int i = 0; i < headers.length; ++i) {
         headerList.put(headers[i], i);
      }

      String jsonString;
      for(jsonString = null; iterator.hasNext(); jsonString = Jsoner.serialize(json)) {
         String[] analyzingRow = (String[])iterator.next();
         String crnString = analyzingRow[(Integer)headerList.get("crn")];
         Integer crn = Integer.valueOf(crnString);
         String subject = analyzingRow[(Integer)headerList.get("subject")];
         String num = analyzingRow[(Integer)headerList.get("num")];
         String description = analyzingRow[(Integer)headerList.get("description")];
         String section = analyzingRow[(Integer)headerList.get("section")];
         String type = analyzingRow[(Integer)headerList.get("type")];
         String creditsString = analyzingRow[(Integer)headerList.get("credits")];
         Integer credits = Integer.valueOf(creditsString);
         String start = analyzingRow[(Integer)headerList.get("start")];
         String end = analyzingRow[(Integer)headerList.get("end")];
         String days = analyzingRow[(Integer)headerList.get("days")];
         String where = analyzingRow[(Integer)headerList.get("where")];
         String schedule = analyzingRow[(Integer)headerList.get("schedule")];
         String instructor = analyzingRow[(Integer)headerList.get("instructor")];
         String[] courseParts = num.split("\\s+", 2);
         String trimmednum1 = courseParts[0];
         String trimmednum2 = courseParts.length > 1 ? courseParts[1] : "";
         JsonObject populatedcourseMap = new JsonObject();
         populatedcourseMap.put("subjectid", trimmednum1);
         populatedcourseMap.put("num", trimmednum2);
         populatedcourseMap.put("description", description);
         populatedcourseMap.put("credits", credits);
         courseMap.put(num, populatedcourseMap);
         List<String> instructorList = Arrays.asList(instructor.split(", "));
         JsonObject populatedsectionList = new JsonObject();
         populatedsectionList.put("crn", crn);
         populatedsectionList.put("subjectid", trimmednum1);
         populatedsectionList.put("num", trimmednum2);
         populatedsectionList.put("section", section);
         populatedsectionList.put("type", type);
         populatedsectionList.put("start", start);
         populatedsectionList.put("end", end);
         populatedsectionList.put("days", days);
         populatedsectionList.put("where", where);
         populatedsectionList.put("instructor", instructorList);
         sectionList.add(populatedsectionList);
         scheduletypeMap.put(type, schedule);
         subjectMap.put(trimmednum1, subject);
         json.put("scheduletype", scheduletypeMap);
         json.put("subject", subjectMap);
         json.put("course", courseMap);
         json.put("section", sectionList);
      }

      return jsonString;
   }

   public String convertJsonToCsvString(JsonObject json) {
      JsonObject scheduletype = (JsonObject)json.get("scheduletype");
      JsonObject subjects = (JsonObject)json.get("subject");
      JsonObject courses = (JsonObject)json.get("course");
      JsonArray sections = (JsonArray)json.get("section");
      StringWriter writer = new StringWriter();
      CSVWriter csvWriter = new CSVWriter(writer, '\t', '"', '\\', "\n");
      csvWriter.writeNext(new String[]{"crn", "subject", "num", "description", "section", "type", "credits", "start", "end", "days", "where", "schedule", "instructor"});
      Iterator var9 = sections.iterator();

      while(var9.hasNext()) {
         Object sectionObj = var9.next();
         JsonObject section = (JsonObject)sectionObj;
         String crn = String.valueOf(section.get("crn"));
         String subjectID = (String)section.get("subjectid");
         String subject = (String)subjects.get(subjectID);
         String justnum = (String)section.get("num");
         String num = subjectID + " " + justnum;
         JsonObject innercourse = (JsonObject)courses.get(num);
         String description = (String)innercourse.get("description");
         String sectionId = (String)section.get("section");
         String type = (String)section.get("type");
         BigDecimal creditsValue = (BigDecimal)innercourse.get("credits");
         String credits = creditsValue.toString();
         String start = (String)section.get("start");
         String end = (String)section.get("end");
         String days = (String)section.get("days");
         String where = (String)section.get("where");
         String schedule = (String)scheduletype.get(type);
         JsonArray instructorsArray = (JsonArray)section.get("instructor");
         List<String> instructorsList = new ArrayList();
         Iterator var30 = instructorsArray.iterator();

         while(var30.hasNext()) {
            Object instructorObj = var30.next();
            instructorsList.add(instructorObj.toString());
         }

         String instructor = String.join(", ", instructorsList);
         csvWriter.writeNext(new String[]{crn, subject, num, description, sectionId, type, credits, start, end, days, where, schedule, instructor});
      }

      String csvString = writer.toString();
      return csvString;
   }

   public JsonObject getJson() {
      JsonObject json = this.getJson(this.getInputFileData("jsu_sp24_v1.json"));
      return json;
   }

   public JsonObject getJson(String input) {
      JsonObject json = null;

      try {
         json = (JsonObject)Jsoner.deserialize(input);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return json;
   }

   public List<String[]> getCsv() {
      List<String[]> csv = this.getCsv(this.getInputFileData("jsu_sp24_v1.csv"));
      return csv;
   }

   public List<String[]> getCsv(String input) {
      List csv = null;

      try {
         CSVReader reader = (new CSVReaderBuilder(new StringReader(input))).withCSVParser((new CSVParserBuilder()).withSeparator('\t').build()).build();
         csv = reader.readAll();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return csv;
   }

   public String getCsvString(List<String[]> csv) {
      StringWriter writer = new StringWriter();
      CSVWriter csvWriter = new CSVWriter(writer, '\t', '"', '\\', "\n");
      csvWriter.writeAll(csv);
      return writer.toString();
   }

   private String getInputFileData(String filename) {
      StringBuilder buffer = new StringBuilder();
      ClassLoader loader = ClassLoader.getSystemClassLoader();

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("resources" + File.separator + filename)));

         String line;
         while((line = reader.readLine()) != null) {
            buffer.append(line).append('\n');
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return buffer.toString();
   }
}
