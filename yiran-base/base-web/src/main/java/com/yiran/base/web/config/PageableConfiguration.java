package com.yiran.base.web.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;

import com.fasterxml.classmate.TypeResolver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;

@Configuration
public class PageableConfiguration {

	@Bean
	public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
		return new AlternateTypeRuleConvention() {

			@Override
			public int getOrder() {
				// TODO Auto-generated method stub
				return Ordered.HIGHEST_PRECEDENCE;
			}

			@Override
			public List<AlternateTypeRule> rules() {
				// TODO Auto-generated method stub
				AlternateTypeRule atr = new AlternateTypeRule(resolver.resolve(Pageable.class),
						resolver.resolve(Page.class));
				List<AlternateTypeRule> list = new ArrayList<AlternateTypeRule>();
				list.add(atr);
				return list;
			}
		};
	}
	
	@ApiModel
	static class Page {
		@ApiModelProperty("第几页(0..N)")
		private Integer page;

		@ApiModelProperty("每页多少条")
		private Integer size;
		
		@ApiModelProperty("排序字段 format:property(,asc|desc).默认为asc，支持多字段分页")
		private List<String> sort;

		public Integer getPage() {
			return page;
		}

		public void setPage(Integer page) {
			this.page = page;
		}

		public Integer getSize() {
			return size;
		}

		public void setSize(Integer size) {
			this.size = size;
		}

		public List<String> getSort() {
			return sort;
		}

		public void setSort(List<String> sort) {
			this.sort = sort;
		}
	}
}
