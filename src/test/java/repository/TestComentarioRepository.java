package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Comentario;
import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestComentarioRepository {
	
	private static final int ID_USUARIO_CONSULTA = 1;
	private static final int ID_USUARIO_SEM_TWEET = 1;
	private static final int ID_USUARIO_SEM_COMENTARIO = 1;
	
	private static final long DELTA_MILIS = 500;
	
	@EJB
	private UsuarioRepository usuarioRepository;
	
	@EJB
	private TweetRepository tweetRepository;
	
	@EJB
	private ComentarioRepository comentarioRepository;
	
	@Before 
	public void setup() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}
	
	@Test
	public void testa_se_comentario_foi_inserido() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		
		Usuario usarioComenta = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		Tweet tweetComentado = this.tweetRepository.consultar(ID_USUARIO_SEM_TWEET);
		
		Comentario comentario = new Comentario();
		
		comentario.setUsuario(usarioComenta);
		comentario.setTweet( tweetComentado );
		
		comentario.setConteudo("Comentando o tweet 1");
		
		this.comentarioRepository.inserir(comentario);
		
		Comentario comentarioInserido = this.comentarioRepository.consultar(ID_USUARIO_SEM_COMENTARIO);
	
		assertThat( comentarioInserido.getId() ).isGreaterThan(0);	
		assertThat( comentarioInserido ).isNotNull();
		assertThat( comentarioInserido.getId() ).isEqualTo(ID_USUARIO_SEM_COMENTARIO);
	}

	@Test
	public void testa_consultar_usuario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_USUARIO_CONSULTA);

		assertThat( comentario ).isNotNull();
		assertThat( comentario.getId() ).isEqualTo(ID_USUARIO_CONSULTA);
		assertThat( comentario.getConteudo() ).isNotNull();
	}
	
	@Test
	public void testa_atualizar_usuario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_USUARIO_CONSULTA);
		
		comentario.setConteudo("Comentario atualizado 1");
		
		this.comentarioRepository.atualizar(comentario);	
		
		comentario = this.comentarioRepository.consultar(ID_USUARIO_CONSULTA);	

		assertThat( comentario ).isNotNull();
		assertThat( comentario.getId() ).isEqualTo(ID_USUARIO_CONSULTA);	
	}
	
	@Test
	public void testa_remover_usuario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_USUARIO_CONSULTA);
		assertThat( comentario ).isNotNull();
		
		this.comentarioRepository.remover(ID_USUARIO_CONSULTA);
		
		Comentario comentarioRemovido = this.comentarioRepository.consultar(ID_USUARIO_CONSULTA);	
		assertThat( comentarioRemovido ).isNull();			
	}
	
	@Test
	public void testa_listar_usuarios() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		List<Comentario> comentarios = new ArrayList<Comentario>();
		
		comentarios = this.comentarioRepository.listarTodos();
		
		assertThat( comentarios ).isNotNull();
		
//		usuarios.forEach(u -> System.out.println(u.getId() + " - " + u.getNome()));
	}
	
}
