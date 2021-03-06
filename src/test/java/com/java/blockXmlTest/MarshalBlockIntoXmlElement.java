package com.java.blockXmlTest;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.java.pojo.Block;

public class MarshalBlockIntoXmlElement {

	public static void main(String[] args) throws JAXBException {
		Date time = new Date();
		Block block = new Block("101", 100, (float)2.50, 200, "Open",
				(float)0.00, time, 500);
		
		File blockFile = new File("blocks.xml");
		
		JAXBContext jaxbContext = JAXBContext.newInstance( Block.class );
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		
		jaxbMarshaller.marshal( block, blockFile );
		//jaxbMarshaller.marshal( block, System.out );
		
		jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
		
		Block unmarshalBlock = (Block) jaxbUnmarshaller.unmarshal(blockFile);
		
		System.out.println(unmarshalBlock.toString());

	}
}
