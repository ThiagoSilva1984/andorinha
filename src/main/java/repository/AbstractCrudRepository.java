package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;

public abstract class AbstractCrudRepository {
	
	// criando a o DS atraves de anotação
	@Resource(name = "andorinhaDS") //essa anotação aqui é uma injeção de dependecia
	protected DataSource ds;
	
	protected int recuperaProximoValorDaSequence(String nomeSequence) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {		
		//vai abria aconexão direto no try e vai fecha sozinho
		try (Connection c = ds.getConnection()){ //abrindo a conexão aqui, o java vai fechar auto-magicamente para mim (isso é try-whith-resource)
			//como recuperar valor do id?
			PreparedStatement ps = c.prepareStatement("select nextval(?)"); //quary para pegar o proximo id
			ps.setString(1, nomeSequence); //aqui estou pasando para a query que fiz em cima o parametro "seq_usuario"
			ResultSet rs = ps.executeQuery(); //aqui eu estou executando os dois comandos de cima (a query)
			if (rs.next()) {
				return rs.getInt(1);
			}
			throw new ErroAoConsultarBaseException("Erro ao recuperar próximo valor da sequence" + nomeSequence, null);	
		} catch (SQLException e) {
			throw new ErroAoconectarNaBaseException("Ocorreu um erro ao acessar a base de dados", e);
		}
	}
}
