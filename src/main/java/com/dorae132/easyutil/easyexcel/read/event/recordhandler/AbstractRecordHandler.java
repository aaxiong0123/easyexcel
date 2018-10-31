package com.dorae132.easyutil.easyexcel.read.event.recordhandler;

import org.apache.poi.hssf.record.Record;

/**
 * the handler that process the record for the xls file
 * @author Dorae
 *
 * @param <T> The target type of the cell.
 */
public abstract class AbstractRecordHandler {

	protected AbstractRecordHandler next;
	
	public AbstractRecordHandler setNext(AbstractRecordHandler next) {
		this.next = next;
		return next;
	}
	
	/**
	 * handle
	 * @param handlerContext
	 * @param record
	 */
	protected void handle(IRecordHandlerContext handlerContext, Record record) throws Exception {
		if (this.couldDecode(handlerContext, record)) {
			this.decode(handlerContext, record);
		} else if (next != null) {
			next.handle(handlerContext, record);
		} else {
			// just do nothing
			return;
		}
	}
	
	/**
	 * could decode or not
	 * @param handlerContext
	 * @param record
	 * @return
	 */
	protected abstract boolean couldDecode(IRecordHandlerContext handlerContext, Record record);
	
	/**
	 * decode
	 * @param handlerContext
	 * @param record
	 */
	protected abstract void decode(IRecordHandlerContext handlerContext, Record record) throws Exception;
}
