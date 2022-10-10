package repository;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;

import runner.DatabaseHelper;

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

}
