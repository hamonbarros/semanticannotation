INSERT INTO planodeconta (codigo, descricao) VALUES ('01.10', 'Equipamentos de TI');
INSERT INTO planodeconta (codigo, descricao) VALUES ('01.15', 'Mobiliario');
INSERT INTO planodeconta (codigo, descricao) VALUES ('01.20', 'Audio e Video');

INSERT INTO fornecedor (id, cnpj, nome) VALUES (1, '11111111111', 'Fornecedor Ilicito');
INSERT INTO fornecedor (id, cnpj, nome) VALUES (2, '22222222222', 'Giroflex');
INSERT INTO fornecedor (id, cnpj, nome) VALUES (3, '33333333333', 'Tudo em Imagem');

INSERT INTO nota (id, dataregistro, situacao) VALUES (1, now(), 2);
INSERT INTO nota (id, dataregistro, situacao) VALUES (2, now(), 2);
INSERT INTO nota (id, dataregistro, situacao) VALUES (3, now(), 2);

INSERT INTO notalancamento (datalancamento, data, numero, uf, id, fornecedor_id) VALUES (to_date('12/18/2013', 'MM/DD/YYYY'), now(), '904305', 'RN', 1, 1);
INSERT INTO notalancamento (datalancamento, data, numero, uf, id, fornecedor_id) VALUES (to_date('12/20/2013', 'MM/DD/YYYY'), now(), '803305', 'RN', 2, 2);
INSERT INTO notalancamento (datalancamento, data, numero, uf, id, fornecedor_id) VALUES (to_date('12/23/2013', 'MM/DD/YYYY'), now(), '901205', 'RN', 3, 3);

INSERT INTO patrimonio (orgao, registro, descricao, situacao, valoraquisicao, planodeconta_codigo, notalancamento_id) VALUES ('DATAPREV', 1, 'PC DO MILHAO-I', 1, 500.0, '01.10', 1);
INSERT INTO patrimonio (orgao, registro, descricao, situacao, valoraquisicao, planodeconta_codigo, notalancamento_id) VALUES ('DATAPREV', 2, 'PC DO MILHAO-II', 1, 600.0, '01.10', 1);
INSERT INTO patrimonio (orgao, registro, descricao, situacao, valoraquisicao, planodeconta_codigo, notalancamento_id) VALUES ('DATAPREV', 3, 'PC DO MILHAO-III', 1, 700.0, '01.10', 1);
INSERT INTO patrimonio (orgao, registro, descricao, situacao, valoraquisicao, planodeconta_codigo, notalancamento_id) VALUES ('DATAPREV', 4, 'PC DO MILHAO-IIII', 1, 800.0, '01.10', 1);

INSERT INTO patrimonio (orgao, registro, descricao, situacao, valoraquisicao, planodeconta_codigo, notalancamento_id) VALUES ('DATAPREV', 5, 'Cadeira Uncomfort Plus', 1, 50.0, '01.15', 2);
INSERT INTO patrimonio (orgao, registro, descricao, situacao, valoraquisicao, planodeconta_codigo, notalancamento_id) VALUES ('DATAPREV', 6, 'Cadeira Noisy 2013', 1, 40.0, '01.15', 2);
INSERT INTO patrimonio (orgao, registro, descricao, situacao, valoraquisicao, planodeconta_codigo, notalancamento_id) VALUES ('DATAPREV', 7, 'Cadeira Uncomfort Noisy', 1, 30.0, '01.15', 2);

INSERT INTO patrimonio (orgao, registro, descricao, situacao, valoraquisicao, planodeconta_codigo, notalancamento_id) VALUES ('DATAPREV', 8, 'Projetor Sony FULL HD', 1, 5000.0, '01.20', 3);

INSERT INTO patrimonio_garantias VALUES ('DATAPREV', 1, now(), 12);
INSERT INTO patrimonio_garantias VALUES ('DATAPREV', 2, now(), 12);