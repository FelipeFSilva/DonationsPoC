CREATE TABLE donation (
	id BIGINT IDENTITY(1,1) NOT NULL,
	status VARCHAR(8) NOT NULL,
	value_paid MONEY NOT NULL,
	donor_name VARCHAR(255) NOT NULL,
    type_person VARCHAR(2) NOT NULL,
    document_number VARCHAR(14) NOT NULL,
    email VARCHAR(100) NOT NULL,
	created_at DATETIME NOT NULL,
	updated_at DATETIME NOT NULL,
	link_billet VARCHAR(255),
	CONSTRAINT PK_donation PRIMARY KEY(id)
);