package com.xblog.chat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xblog.chat.aspect.ExtractType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChatRoomMemberUpdate {
	ExtractType value() default ExtractType.AFTER;
}
