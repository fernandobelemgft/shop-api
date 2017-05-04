package com.shop.rest.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shop.exception.LocationNotFoundException;
import com.shop.model.Shop;
import com.shop.model.ShopAddress;
import com.shop.service.shops.ShopService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class ShopControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private ShopService shopService;

	@Before
	public void setUp() throws LocationNotFoundException {
		
		shopService.eraseDatabase();
		shopService.saveShop(new Shop("Newcastle Shop", new ShopAddress("1", "NE1 1AD")));
		shopService.saveShop(new Shop("Derby Shop", new ShopAddress("1", "DE1 0GR")));
		shopService.saveShop(new Shop("Oxford Shop", new ShopAddress("1", "OX1 1AA")));
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
        
	}

	@Test
	public void createShopTest() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
        
        ObjectNode shopNode = mapper.createObjectNode();
        shopNode.put("shopName", "Oxford Shop");
        
        ObjectNode addressNode = mapper.createObjectNode();
        addressNode.put("number", "1");
        addressNode.put("postCode", "OX1 1AA");
        shopNode.putPOJO("shopAddress", addressNode);

		MockHttpServletRequestBuilder builder =
                 MockMvcRequestBuilders.post("/shops")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(shopNode.toString());

		System.out.println("CONTENT: " + this.mockMvc.perform(builder).andReturn().getResponse().getContentAsString());
		this.mockMvc.perform(builder)
		         .andExpect(MockMvcResultMatchers.status()
		                                         .isCreated());
	}
	
//	@Ignore
//	@Test(expected=LocationNotFoundException.class)
//	public void createShopWithInvalidDataTest() throws Exception {
//		
//		ObjectMapper mapper = new ObjectMapper();
//        
//        ObjectNode shopNode = mapper.createObjectNode();
//        shopNode.put("shopName", "Oxford Shop");
//        
//        ObjectNode addressNode = mapper.createObjectNode();
//        addressNode.put("number", "1");
//        addressNode.put("postCode", "ERROR ADDRESS");
//        shopNode.putPOJO("shopAddress", addressNode);
//
//		MockHttpServletRequestBuilder builder =
//                 MockMvcRequestBuilders.post("/shops")
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(shopNode.toString());
//		this.mockMvc.perform(builder);
//	}
	
	@Test
	public void findNearestTest() throws Exception{
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/findNearest?latitude=51.7279805&longitude=-1.0259525");
		
		System.out.println("Find Nearest: " + this.mockMvc.perform(builder)
				.andReturn().getResponse().getContentAsString());
		
	}


}
