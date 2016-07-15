#!/bin/bash

export AWS_DB_PW=Zypjno4Ou0aqBsMFwEFbJGYhsVcInSMp0TAkBgarD;

mysqldump -h boxdb-kilroy91841-prod-homer-good-0.cuuqhlvpkffi.eu-central-1.rds.amazonaws.com -u wARgpjR2mCIMhGhp -p$AWS_DB_PW --port=3306 --databases homer | mysql -h localhost -u root --port 3306