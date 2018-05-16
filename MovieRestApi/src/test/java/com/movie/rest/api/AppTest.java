package com.movie.rest.api;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.primitives.Ints;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class AppTest {
	static RequestSpecBuilder builder;
	static RequestSpecification requestSpec;

	@BeforeClass
	public static void init() {
		// building the api path
		RestAssured.baseURI = "https://splunk.mocklab.io";
		
		builder = new RequestSpecBuilder();
		builder.addQueryParam("q","batman");
		builder.addHeader("Accept", "application/json");
		
		requestSpec = builder.build();
		
	}
//TestCase:SPL-001
// TestCase: No two movies should have the same image 	
//from the reposnse split the the string that contains .jpg 
//Loop through them and Add them to list and set. Then Compare two objects using assertequals()	
	//Our test returns passed if two Collections are not same 
	@Test
	public void SP001() throws Exception{
		final String SEPARATOR = ",";
		List<String> poster_path_image; 
		poster_path_image= given()
		.spec(requestSpec)
		.when()
		.get("/movies")
		.then()
		.extract()
		.path("results.poster_path");
		
		
		//convert the list to string 
		StringBuilder poster = new StringBuilder();
		  for(String str : poster_path_image){
		    poster.append(str);
		    poster.append(SEPARATOR);
		  }
		  String st = poster.toString();
		  st = st.substring(0, st.length() - SEPARATOR.length());
		  
		  String[]  poster_split=st.split("[/,]");
		  
		  //System.out.println(Arrays.toString(poster_split));
		 // System.out.println(poster_split[2]);
		  List <String> list=new ArrayList<String>();
		  Set <String> set =new LinkedHashSet<String>();
		  
		  for (int i = 0; i < poster_split.length; i++) {			 
			  if (poster_split[i].contains("jpg"))
				  list.add(poster_split[i]);
		  }
		  for (int j = 0; j < poster_split.length; j++) {
			  if (poster_split[j].contains("jpg"))
				  set.add(poster_split[j]);
			
		}
		
		assertEquals(list, set);    			
		  
	}
	
	//TestCase:SPL-002	
	@Test
	public void SP002() throws Exception  {
		List<String> poster_path_image2;
 
		poster_path_image2= given()
		.spec(requestSpec)
		.when()
		.get("/movies")
		.then()
		.extract()
		.path("results.poster_path");
		
		String s= poster_path_image2.toString();
	// link is valid when it starts with https:// also a null is acceptable.
	//	Method fails becasue one line starts with just "/". 
				  try {
				assertThat(s, anyOf(startsWith("https://www.dropbox.com"),isEmptyOrNullString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
		
	}
	//TestCase:SPL-003
	@Test
	public void SP003() throws Exception  {
		List<Integer> genre_id;
		List <Integer>id;
 
		genre_id= given()
		.spec(requestSpec)
		.when()
		.get("/movies")
		.then()
		.extract()
		.path("results.genre_ids");
		
		// exttract the id value now
		id= given()
				.spec(requestSpec)
				.when()
				.get("/movies")
				.then()
				.extract()
				.path("results.id");
		
		
		//covert genre_id to to string so that we can split it.
		String str= genre_id.toString();
		String []st = str.split(",");
		
		//covert id to to string so that we can split it.
		String str_id=id.toString();
		String [] st_id=str_id.split(",");
		
		//Store first two values of id and then compare if s2 is less than s1, if it is less then they are in ascending order.
		String s1=st_id[0];
		String s2=st_id[1];		
		
		//Loop through each element and check if it is null
		//if null check first response i.e genre_id is null
		for ( int i = 0; i <= st.length-1; i++) {
			if (st[i]==null) {
				assertThat(st[0], nullValue());
				assertThat(s1, lessThan(s2));	
			}
		}			
	}
		
		@Test
		public void SPL004() throws Exception{
			ArrayList<ArrayList<Integer>> genre_id=new ArrayList<ArrayList<Integer>>();
					
			genre_id=given()
				.spec(requestSpec)
				.when()
				.get("/movies")
				.then()
				.extract()
				.path("results.genre_ids");
		//	System.out.println(genre_id);
			
	         	int sum=0;
	         	int count=0;
	         	for (int i = 0; i < genre_id.size(); i++){
	         		
	         		sum=0;
	         		for(int j=0;j<genre_id.get(i).size();j++) {
	         			sum=sum+genre_id.get(i).get(j);
	         				         			
	         		}
	         		if (sum>400) {
	   				 count=count+1;
	   			 }	
			}
    			//System.out.println(count);
    						
    			assertEquals(7, count);

								
	}
		
		@Test
		public void SPL005() throws Exception{
			List<String> title;
					
			title=given()
				.spec(requestSpec)
				.when()
				.get("/movies")
				.then()
				.extract()
				.path("results.title");
			
			//Convert List to String array and then split using white space so that we have each word separate.
			String st= title.toString();			
			String [] str_array=st.split("\\s+");
		
			//Make a new array to store the values after reverse. 
		    String[] reverse = new String[str_array.length];
		    
			for(int i=0;i<str_array.length;i++) {
			      StringBuilder sb = new StringBuilder(str_array[i]);
			      reverse [i]= sb.reverse().toString();   
			}
			
			// Now compare two arrays and keep a count for palindrome.
			int count =0;
			for (int i = 0; i < str_array.length; i++) {
				
				for (int j = 0; j < reverse.length; j++) {
					
					if(str_array[i].equals(reverse[j])) {
						count = count+1;
					}	
				}
			}
			//System.out.println(count);	
			//Check the palindrome count is equal to 1 as per the document.
			assertEquals(1, count); 
			} 
		
		
		
		@Test
		public void SPL006() throws Exception{
			List<String> title;
					
			title=given()
				.spec(requestSpec)
				.when()
				.get("/movies")
				.then()
				.extract()
				.path("results.title");
			//System.out.println(title);
			String st= title.toString();			
			String [] str_array=st.replaceAll(":","").trim().split(",");
			
			int count =0;
			for (int i = 0; i < str_array.length; i++) {
				for (int j = i+1; j < str_array.length; j++) {
					if(str_array[i].contains(str_array[j])) {
						count= count+1;
					}
					
				}
				
			}	
			assertEquals(2, count);
			//System.out.println("The Count is" + count );
		
		}	

}						
			
		
	
		
	