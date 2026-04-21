package com.projetinho.projetinho_de_recapitulacao.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) { }
