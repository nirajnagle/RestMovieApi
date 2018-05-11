package com.movie.rest.api;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.testng.asserts.SoftAssert;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsArrayContaining;
import org.hamcrest.core.AnyOf;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
		  
		  System.out.println(Arrays.toString(poster_split));
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
		//System.out.println(poster_split[i]);
		 // System.out.println(list);
		  //System.out.println("===============");
		 // System.out.println(set);
		assertEquals(list, set);    			
		  
	}
	
	
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
		//System.out.println(s);
	
				  try {
				assertThat(s, anyOf(startsWith("https://"),isEmptyOrNullString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
		
	}
	
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


		
		//genre_id== null are not first in reposnse
		
		//Loop through each element and check if it is null
		//if null check first response i.e genre_id is null
		for ( int i = 0; i <= st.length; i++) {
				//System.out.println(st[i]);
			if (st[i]==null) {
				assertThat(st[0], nullValue());
				assertThat(s1, lessThan(s2));	
			}
		}

			
			
			
			

			
		}
		
				
		
		
	
	}
		
	