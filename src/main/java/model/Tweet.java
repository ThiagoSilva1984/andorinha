package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

public class Tweet {
	
	private int id;
	private String conteudo;
	private Calendar data_postagem;
	private Usuario usuario;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getConteudo() {
		return conteudo;
	}
	
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Calendar getData_postagem() {
		return data_postagem;
	}

	public void setData_postagem(Calendar data_postagem) {
		this.data_postagem = data_postagem;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
