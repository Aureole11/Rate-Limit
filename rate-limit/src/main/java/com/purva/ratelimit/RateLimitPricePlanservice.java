package com.purva.ratelimit;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucket;

/**
 * provide number of tokens in bucket
 * according to price plans by customer
 * @author Niks
 *
 */

@Service
public class RateLimitPricePlanservice {

	@Value("${rate.limit.client.basic}")
	private String basic;
	
	@Value("${rate.limit.client.free}")
	private String free;
	
	public Bucket getPlanServiceBucket(String clientToken) {
		LocalBucket data = Bucket4j.builder()
		.addLimit(getClientBandwidth(clientToken))
		.build();
		
		System.out.println(data);
		return data;
	}

	private Bandwidth getClientBandwidth(String clientToken) {
		if(clientToken.equals(basic))//basic plan customers	
		//refil - rate at which token will be filled in the bucket
		return Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
		else if(clientToken.equals(free))//premium customers
			return Bandwidth.classic(25, Refill.intervally(25, Duration.ofMinutes(1)));
		return Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(1)));
	}
}
