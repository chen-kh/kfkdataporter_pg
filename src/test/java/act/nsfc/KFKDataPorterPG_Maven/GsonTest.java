package act.nsfc.KFKDataPorterPG_Maven;

import java.io.StringReader;
import java.nio.channels.NonWritableChannelException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import act.nsfc.kfkDataPorterPG.bean.GPS;
import act.nsfc.kfkDataPorterPG.bean.OBD;
import scala.xml.dtd.PublicID;

public class GsonTest {
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		Dog dog = new Dog();
		System.out.println(dog.toString());
		GPS gps = new GPS();
		OBD obd = new OBD();
		System.out.println(gps.toString());
		System.out.println(obd.toString());
		String dog2 = "{\"name\":\"dog\"}";
		String string = "";
//		obd = gson.fromJson(string, OBD.class);
		JsonElement jsonElement = gson.toJsonTree(obd);
		JsonReader jsonReader = new JsonReader(new StringReader(dog2));
//		jsonReader.
		
		if(jsonElement.isJsonNull() ){
			System.out.println("json nullllllllllllllllllllllllllllllllllllllllllll");
		}
		
		
		
		System.out.println("-------------------------------------------");
		System.out.println(gson.fromJson(obd.toString(),OBD.class));
	}
}
class Dog{
	String name;
	String color;
	public Dog() {
		this.name = "dog";
		this.color = "write";
	}
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
