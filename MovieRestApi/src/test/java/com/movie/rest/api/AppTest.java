package com.movie.rest.api;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
// using the Rest Assured Library extract the value poster_path from Api
//response, once we have response we spilt it and store in array. Later we test using method assertnotsame() that two are not same image names.
	//Our test returns passed if two Collections are not same 
	@Test
	public void SP001() {
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
		  
		 String [] arr= {"cinema-food-movvvie-theater-33129.jpg,actor-actress-adult-974477.jpg?dl=0adult-beard-electronics-819848.jpg?dl=0,architecture-bluebird-theatre-building-208647.jpg?dl=0,black-and-white-caixa-belas-artes-cinema-65437.jpg,candy-delicious-eating-2904.jpg?dl=0,cinema-food-movie-theater-33129.jpg?dl=0,cinema-food-movie-theater-33129.jpg?dl=0,sWa1Y5QhGuJMjw8uuFoggGLqZ0y.jpg,cinema-food-movvvie-theater-33129.jpg?dl=0"};

		 
		  List<String> st1= new ArrayList<String>();
		  		st1.add(poster_split[5]);
		  		st1.add(poster_split[11]);				
		  		st1.add(poster_split[17]);				
		  		st1.add(poster_split[23]);				
		  		st1.add(poster_split[29]);				
		  		st1.add(poster_split[35]);				
		  		st1.add(poster_split[41]);				
		  		st1.add(poster_split[47]);				
		  		st1.add(poster_split[53]);				
		  		st1.add(poster_split[55]);
		  		st1.add(poster_split[62]);			
			
		  assertNotSame(st1, Arrays.toString(arr));
	}
}	