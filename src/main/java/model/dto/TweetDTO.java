package model.dto;

import java.util.Calendar;

public class TweetDTO {

	private int id;
	private int idUsuario;
	private String conteudo;
	private String nomeUsuario;
	private Calendar dataPostagem;

	public TweetDTO() {
		super();
	}

	public TweetDTO(int id, int idUsuario, String conteudo, String nomeUsuario, Calendar dataPostagem) {
		super();
		this.id = id;
		this.idUsuario = idUsuario;
		this.conteudo = conteudo;
		this.nomeUsuario = nomeUsuario;
		this.dataPostagem = dataPostagem;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public Calendar getDataPostagem() {
		return dataPostagem;
	}

	public void setDataPostagem(Calendar dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

}
