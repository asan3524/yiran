package com.yiran.base.feign.config;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import feign.Contract;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;

@Configuration
public class GsonFeignConfiguration {
	public class GsonDecoder implements Decoder {

		private final Gson gson;

		public GsonDecoder(Gson gson) {
			this.gson = gson;
		}

		@Override
		public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
			// TODO Auto-generated method stub
			if (response.status() == 404)
				return Util.emptyValueOf(type);
			if (response.body() == null)
				return null;
			Reader reader = response.body().asReader();
			try {
				return gson.fromJson(reader, type);
			} catch (JsonIOException e) {
				if (e.getCause() != null && e.getCause() instanceof IOException) {
					throw IOException.class.cast(e.getCause());
				}
				throw e;
			} finally {
				reader.close();
			}
		}
	}

//	@Bean
//	public Contract feignContract() {
//		return new feign.Contract.Default();
//	}

	@Bean
	public Decoder feignDecoder() {
		Gson gson = new Gson();
		return new GsonDecoder(gson);
	}
}
