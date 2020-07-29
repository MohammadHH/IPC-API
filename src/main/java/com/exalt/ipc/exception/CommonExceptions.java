package com.exalt.ipc.exception;

import org.springframework.http.HttpStatus;

public class CommonExceptions {
	public static final int E6010 = 6010;

	public static final int E6011 = 6011;

	public static final int E6020 = 6020;

	public static final int E6021 = 6021;

	public static final int E8030 = 8030;

	public static final int E8031 = 8031;

	public static final int E8040 = 8040;

	public static final int E8041 = 8041;

	public static final int E8050 = 8050;

	public static final int E8051 = 8051;

	public static final int E8052 = 8052;

	public static final int E8053 = 8053;

	public static final int E8080 = 8080;

	public static final int E9010 = 9010;

	public static final int E9011 = 9011;

	public static final int E9012 = 9012;

	public static final int E9013 = 9013;

	public static final int E9040 = 9040;

	public static final int E9041 = 9041;

	public static final int E9050 = 9050;

	public static final int E9051 = 9051;

	public static final int E9052 = 9052;

	public static final int E9053 = 9053;

	public static final int E9060 = 9060;

	public static final int E9061 = 9061;

	public static final int E9062 = 9062;


	public static final ApiException USER_REGISTRATION_FAILED_EXCEPTION =
			new ApiException(HttpStatus.UNAUTHORIZED, E6010, E6011);

	public static final ApiException AUTHENTICATION_FAILED_EXCEPTION =
			new ApiException(HttpStatus.UNAUTHORIZED, E6020, E6021);

	public static final ApiException PRESS_NOT_FOUND_EXCEPTION = new ApiException(HttpStatus.NOT_FOUND, E8080);

	public static final ApiException PRESS_ALREADY_MAPPED_EXCEPTION =
			new ApiException(HttpStatus.BAD_REQUEST, E8030, E8031);

	public static final ApiException PRESS_ALREADY_UNMAPPED_EXCEPTION =
			new ApiException(HttpStatus.BAD_REQUEST, E8040, E8041);

	public static final ApiException NO_MAPPED_PRESS_EXCEPTION = new ApiException(HttpStatus.BAD_REQUEST, E8050, E8051);

	public static final ApiException NO_MAPPED_PRESS_EXCEPTION2 = new ApiException(HttpStatus.BAD_REQUEST, E8051);

	public static final ApiException NO_JOB_TO_PRINT_EXCEPTION = new ApiException(HttpStatus.BAD_REQUEST, E8050, E8052);

	public static final ApiException NO_ROOM_IN_PRINTING_QUEUE_EXCEPTION =
			new ApiException(HttpStatus.BAD_REQUEST, E8050, E8053);

	public static final ApiException NO_ROOM_IN_IPC_QUEUE_EXCEPTION =
			new ApiException(HttpStatus.BAD_REQUEST, E9010, E9011);

	public static final ApiException EMPTY_UPLOADED_FILE_EXCEPTION =
			new ApiException(HttpStatus.BAD_REQUEST, E9010, E9012);

	public static final ApiException INVALID_FILE_MIME_EXCEPTION =
			new ApiException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, E9010, E9013);

	public static final ApiException JOB_NOT_FOUND_EXCEPTION = new ApiException(HttpStatus.NOT_FOUND, E9060, E9061);

	public static final ApiException NULL_JOB_IDS_EXCEPTION = new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, E9062);

	public static final ApiException INVALID_JOB_STATE_EXCEPTION =
			new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, E9040, E9041);

	public static final ApiException INVALID_JOB_SOURCE_EXCEPTION =
			new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, E9050, E9051);

	public static final ApiException INVALID_JOB_DESTINATION_EXCEPTION =
			new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, E9052, E9053);

}
