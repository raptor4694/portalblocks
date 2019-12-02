package com.raptor.portalblocks.blocks;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface IVariantHolder {
	
	String getVariantName(int meta);
	
	int variantCount();
	
	default Iterable<String> getVariantNames() {
		return () -> new Iterator<String>() {
			int pos = 0;
			
			@Override
			public boolean hasNext() {
				return pos < variantCount();
			}
			
			@Override
			public String next() {
				if(pos >= variantCount())
					throw new NoSuchElementException();
				return getVariantName(pos++);
			}
		};
	}
	
}
