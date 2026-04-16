package com.example.shipfast.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErroResponseDTO {

    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String mensagem;
    private List<String> detalhes;

    public ErroResponseDTO(int status, String erro, String mensagem, List<String> detalhes) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.detalhes = detalhes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }
}
