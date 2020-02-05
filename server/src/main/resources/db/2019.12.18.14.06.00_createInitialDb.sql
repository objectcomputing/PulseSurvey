drop table if exists responsekeys;
CREATE TABLE responsekeys(
   responseKey varchar PRIMARY KEY,
   issuedOn TIMESTAMPTZ,
   used boolean
);

drop table if exists response;
CREATE TABLE response(
   responseId varchar PRIMARY KEY,
   responseKey varchar REFERENCES responsekeys(responseKey),
   selected varchar,
   createdOn TIMESTAMPTZ
);

drop table if exists userComments ;
CREATE TABLE userComments (
   commentId varchar PRIMARY KEY,
   responseKey varchar REFERENCES responsekeys(responseKey),
   commentText VARCHAR,
   createdOn TIMESTAMPTZ
);
