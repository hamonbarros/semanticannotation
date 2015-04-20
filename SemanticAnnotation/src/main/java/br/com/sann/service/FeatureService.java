package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.SpatialData;

/**
 * Interface de neg�cio relacionada com Feature Types.
 * 
 * @author Hamon
 *
 */
public interface FeatureService {
	
	/**
	 * Recupera todos os SpatialData cadastrados.
	 * 
	 * @return Os SpatialData cadastrados.
	 */
	public List<SpatialData> recoverAllSpatialData();
	
	/**
	 * Recupera todos os SpatialData cadastrados n�o anotados.
	 * 
	 * @return Os SpatialData cadastrados n�o anotados.
	 */
	public List<SpatialData> recoverAllSpatialDataNotAnnotated();

	/**
	 * Recupera a bag of words constru�da a partir dos SpatialData com um 
	 * determinado t�tulo.
	 * 
	 * @param title O t�tulo a ser pesquisado.
	 * @return A bag of words constu�da com as informa��es textuais do SpatialData.
	 */
	public String recoverBagOfWordsByTitle(String title);
	
	/**
	 * Atualiza a coluna "annotated" da lista de spatialdata na base de dados.
	 * @param list A lista a ser atualizada.
	 */
	void updateSpatialDataList(List<SpatialData> list);
	
	/**
	 * Atualiza a coluna "annotated" do spatialdata na base de dados.
	 * @param spatialData O spatialData a ser atualizada.
	 */
	void updateSpatialData(SpatialData spatialData);
	
	/**
	 * Recupera os SpatialData a partir dos IDs passados.
	 * @param ids IDs dos spatialData a serem recuperados.
	 * @return Os SpatialData solicitados.
	 */
	public List<SpatialData> recoverySpatialDataByIDs(String ids);
	
	/**
	 * Recupera um SpatialData a partir do nome, titulo, descri��o e keywords do FT.
	 * @param name O nome do FT.
	 * @param title O titulo do FT.
	 * @param textDescription A descri��o do FT.
	 * @param keywords As palavras chaves do FT.
	 * @return Os feature types recuperados.
	 */
	public List<SpatialData> recoverySpatialDataByTextInfo(String name, String title, 
			String textDescription, String keywords);
}
