package org.example.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    @NotBlank
    @Length(max = 1, message = "格式錯誤，請輸入1~7")
    @Pattern(regexp = "^[0-7]*$", message = "格式錯誤，請輸入數字1~7")
    private String requestType;

    @Valid
    private MgniRequest request;

}
