

create extension pgcrypto;
CREATE TABLE responseKeys(
   responseKey UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   issuedOn TIMESTAMPTZ
);

CREATE TABLE response(
   responseId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   responseKey UUID REFERENCES keys(responseKey),
   selected INTEGER,
   createdOn TIMESTAMPTZ
);

CREATE TABLE userComments (
   commentId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   responseKey UUID REFERENCES keys(responseKey),
   commentText VARCHAR,
   createdOn TIMESTAMPTZ
);
