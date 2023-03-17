package com.purva.ratelimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bucket4j.Bucket;

@RestController
@RequestMapping("/rate-limit")
public class RateLimitController {
	
	@Autowired
	private RateLimitPricePlanservice ratelimit;
	
	private Bucket bucket = null;
	
	/**
	 * to generate token using api
	 * @return
	 */
	@GetMapping("/token-generate/{clientToken}")
	public 	ResponseEntity<String> generateToken(@PathVariable("clientToken") String clientToken){
		//refil - rate at which token will be filled in the bucket	
		//bucket - bucket is interface and its implementation is bucket4j
		bucket=ratelimit.getPlanServiceBucket(clientToken);

		return new ResponseEntity<String>("Generated Successfully||| \n"+ bucket.toString(), HttpStatus.OK);
	}
	
	/**
	 * call exact api for demo
	 * @return
	 */

	@GetMapping("/demo")
	public ResponseEntity<String> demo(){
		if(bucket.tryConsume(1))//try to consume 1 token
		{
			System.out.println("=========Api working successfuly=========");
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		}
		System.out.println("=========number of hits exceeded=========");
		return new ResponseEntity<String>("Too many hits !!! Please try later !!!", HttpStatus.TOO_MANY_REQUESTS);
	}
}
