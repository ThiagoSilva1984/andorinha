package model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "comentario")
public class Comentario {

	@Id
	@SequenceGenerator(name = "seq_comentario", sequenceName = "seq_comentario", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_comentario")
	@Column(name = "id")
	private int id;

	@Column(name = "conteudo")
	private String conteudo;

	@Column(name = "data_postagem")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataPostagem;

	@OneToOne
	@JoinColumn(name = "id_usuario", referencedColumnName = "id")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "id_tweet", referencedColumnName = "id")
	private Tweet tweet;

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

	public Calendar getDataPostagem() {
		return dataPostagem;
	}

	public void setDataPostagem(Calendar dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Tweet getTweet() {
		return tweet;
	}

	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}

	@Override
	public String toString() {
		return "\nComentario Id: " + this.getId() 
			 + "\n Conteudo: " + this.getConteudo() 
			 + "\n Data: " + this.getDataPostagem()
			 + "\n Usuario que comentou: " + this.getUsuario().getNome() 
			 + "\n Tweet comentado: " + getTweet().getConteudo();
	}

}
