package com.example.speak_up.business_logic.api;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.speak_up.business_logic.entity.Greeting;
import com.example.speak_up.business_logic.entity.Petition;

@RestController
@RequestMapping("/greetings")
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@GetMapping("/petition")
	public Petition petition(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Petition();
	}
}