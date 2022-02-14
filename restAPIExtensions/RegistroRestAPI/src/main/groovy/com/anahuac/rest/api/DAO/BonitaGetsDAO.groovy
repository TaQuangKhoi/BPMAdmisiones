package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class BonitaGetsDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(BonitaGetsDAO.class)
	Connection con
	Statement stm
	ResultSet rs
	ResultSet rs2
	PreparedStatement pstm
	
}
