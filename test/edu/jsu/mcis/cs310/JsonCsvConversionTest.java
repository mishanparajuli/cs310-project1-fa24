package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import java.util.List;
import java.util.Set;
import org.junit.*;
import java.util.Iterator;
import static org.junit.Assert.*;

public class JsonCsvConversionTest {
   private String csvOriginalString;
   private List<String[]> csvOriginal;
   private JsonObject jsonOriginal;
   private ClassSchedule schedule;

   @Before
   public void setUp() {
      this.schedule = new ClassSchedule();
      this.csvOriginal = this.schedule.getCsv();
      this.csvOriginalString = this.schedule.getCsvString(this.csvOriginal);
      this.jsonOriginal = this.schedule.getJson();
   }

   @Test
   public void testCsvToJson() {
      try {
         String testJsonString = this.schedule.convertCsvToJsonString(this.csvOriginal);
         JsonObject testJsonObject = Jsoner.deserialize(testJsonString, new JsonObject());
         JsonObject testJsonObjectOriginal = Jsoner.deserialize(Jsoner.serialize(this.jsonOriginal), new JsonObject());
         Set<String> keys = testJsonObjectOriginal.keySet();
         keys.remove("section");
         Iterator var5 = keys.iterator();

         while(var5.hasNext()) {
            String key = (String)var5.next();
            Object testValue = testJsonObject.get(key);
            Object originalValue = testJsonObjectOriginal.get(key);
            Assert.assertNotNull(testValue);
            Assert.assertEquals(originalValue, testValue);
         }

         JsonArray sectionTest = (JsonArray)testJsonObject.get("section");
         JsonArray sectionOriginal = (JsonArray)this.jsonOriginal.get("section");
         Assert.assertNotNull(sectionTest);

         for(int i = 0; i < sectionOriginal.size(); ++i) {
            Assert.assertEquals(sectionOriginal.get(i), sectionTest.get(i));
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         Assert.fail();
      }

   }

   @Test
   public void testJsonToCsv() {
      try {
         String testCsvString = this.schedule.convertJsonToCsvString(this.jsonOriginal);
         Assert.assertEquals(this.csvOriginalString, testCsvString);
      } catch (Exception var2) {
         var2.printStackTrace();
         Assert.fail();
      }

   }

   @Test
   public void testCsvToJsonToCsv() {
      try {
         String testJsonString = this.schedule.convertCsvToJsonString(this.csvOriginal);
         JsonObject testJsonObject = Jsoner.deserialize(testJsonString, new JsonObject());
         String testCsvString = this.schedule.convertJsonToCsvString(testJsonObject);
         Assert.assertEquals(this.csvOriginalString, testCsvString);
      } catch (Exception var4) {
         var4.printStackTrace();
         Assert.fail();
      }

   }

   @Test
   public void testJsonToCsvToJson() {
      try {
         String testCsvString = this.schedule.convertJsonToCsvString(this.jsonOriginal);
         List<String[]> testCsv = this.schedule.getCsv(testCsvString);
         String testJsonString = this.schedule.convertCsvToJsonString(testCsv);
         JsonObject testJsonObject = Jsoner.deserialize(testJsonString, new JsonObject());
         JsonObject testJsonObjectOriginal = Jsoner.deserialize(Jsoner.serialize(this.jsonOriginal), new JsonObject());
         Set<String> keys = testJsonObjectOriginal.keySet();
         keys.remove("section");
         Iterator var7 = keys.iterator();

         while(var7.hasNext()) {
            String key = (String)var7.next();
            Object testValue = testJsonObject.get(key);
            Object originalValue = testJsonObjectOriginal.get(key);
            Assert.assertNotNull(testValue);
            Assert.assertEquals(originalValue, testValue);
         }

         JsonArray sectionTest = (JsonArray)testJsonObject.get("section");
         JsonArray sectionOriginal = (JsonArray)this.jsonOriginal.get("section");
         Assert.assertNotNull(sectionTest);

         for(int i = 0; i < sectionOriginal.size(); ++i) {
            Assert.assertEquals(sectionOriginal.get(i), sectionTest.get(i));
         }
      } catch (Exception var11) {
         var11.printStackTrace();
         Assert.fail();
      }

   }
}