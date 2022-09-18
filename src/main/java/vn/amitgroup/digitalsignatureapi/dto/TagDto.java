package vn.amitgroup.digitalsignatureapi.dto;


import groovy.transform.builder.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class TagDto {
    private Integer id;
	private String name;
    private String colorCode;
}
