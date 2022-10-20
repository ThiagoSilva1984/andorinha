package model.seletor;

import java.util.Calendar;
import java.util.Date;

public class TweetSeletor extends AbstractBaseSeletor{

	private Integer id;
	private String conteudo;
	private Calendar dataPostagemInicial;
	private Calendar dataPostagemFinal;
	private Integer idUsuario;

	public boolean possuiFiltro() {
		return ( this.id != null ) || 
			   ( this.conteudo != null && !this.conteudo.trim().isEmpty() ) ||
			   ( this.getDataPostagemInicial() != null ) ||
			   ( this.getIdUsuario() != null ) ||
			   ( this.getDataPostagemFinal() != null );
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Calendar getDataPostagemInicial() {
		return dataPostagemInicial;
	}

	public void setDataPostagemInicial(Calendar dataPostagem) {
		this.dataPostagemInicial = dataPostagem;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Calendar getDataPostagemFinal() {
		return dataPostagemFinal;
	}

	public void setDataPostagemFinal(Calendar dataPostagemFinal) {
		this.dataPostagemFinal = dataPostagemFinal;
	}

}
