import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MobileBill {

	public static void main(String[] args) throws ParseException, JSONException {
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyy");
		DateFormat tf = new SimpleDateFormat("HH:mm:ss");
		ArrayList<Date> dates = new ArrayList<>();
		ArrayList<String> startT = new ArrayList<>();
		ArrayList<String> endT = new ArrayList<>();
		ArrayList<String> mobileN = new ArrayList<>();
		ArrayList<String> pro = new ArrayList<>();
		ArrayList<Integer> callT = new ArrayList<>();
		ArrayList<Double> bill = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader("/Users/nut/Documents/workspace/mfec-assign/src/promotion1.log"));
			String line = null;
			while ((line = br.readLine()) != null) {
				String eachLine[] = line.split("\\|");
				
				//add Date
				Date dateData = df.parse(eachLine[0]);
				dates.add(dateData);
				//add Start and End Time
				startT.add(eachLine[1]);
				endT.add(eachLine[2]);
				//add Mobile Number and Promotion
				mobileN.add(eachLine[3]);
				pro.add(eachLine[4]);
				//add Call Time (Second)
				Date startData = tf.parse(eachLine[1]);
				Date endData = tf.parse(eachLine[2]);
				long timeDiff = endData.getTime()-startData.getTime();
				int hourDiff = (int)(timeDiff/(60*60*1000)%24);
				int minDiff = (int)(timeDiff/(60*1000)%60);
				int secDiff = (int)(timeDiff/1000%60);
				int total = (hourDiff*60*60)+(minDiff*60)+secDiff;
				
				callT.add(total);
			}
			br.close();
			
			//Create JSON
			ArrayList<JSONObject> arrayJSON = new ArrayList<JSONObject>();
			JSONObject object;
			
			for(int i=0; i< callT.size(); i++){
				//Calculate Bill and add to ArrayList Bill
				double total =3;
				if (callT.get(i)>60)
					total=total+((callT.get(i)-60)/60.00);
				bill.add(total);
				
				//Add data into JSON
				object = new JSONObject();
				object.put("mobileN",mobileN.get(i));
				object.put("bill", bill.get(i));
				arrayJSON.add(object);
			}
			//Creating JSON array
			JSONArray json = new JSONArray(arrayJSON);
			
			//Write JSON file
			File makeFile = new File("/Users/nut/Documents/workspace/mfec-assign/billJSON.json");
			FileWriter writer;
			
			writer = new FileWriter(makeFile,true);
			writer.write(json.toString());
			writer.close();
			System.out.println("Writing success!!");
			
			
		}	catch (IOException e) {
			System.err.println(e.getMessage());
			
		}
	}

}
