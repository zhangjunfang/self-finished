/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.database.sequence.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.config.sequence.Sequence;
import org.tinygroup.database.config.sequence.Sequences;
import org.tinygroup.database.sequence.SequenceProcessor;
import org.tinygroup.database.sequence.SequenceSqlProcessor;

public class SequenceProcessorImpl implements SequenceProcessor {

	private Map<String, Sequence> sequenceMap = new HashMap<String, Sequence>();
	private ProcessorManager processorManager;
	
	
	public ProcessorManager getProcessorManager() {
		return processorManager;
	}

	public void setProcessorManager(ProcessorManager processorManager) {
		this.processorManager = processorManager;
	}

	public void addSequences(Sequences sequences) {
		for (Sequence sequence : sequences.getSequences()) {
			sequenceMap.put(sequence.getName(), sequence);
		}
	}

	public void removeSequences(Sequences sequences) {
		for (Sequence sequence : sequences.getSequences()) {
			sequenceMap.remove(sequence.getName());
		}
	}

	public Sequence getSequence(String sequenceName) {
		return sequenceMap.get(sequenceName);
	}

	public String getCreateSql(String sequenceName, String language) {
		Sequence sequence = getSequence(sequenceName);
		if (sequence == null) {
			throw new RuntimeException(String.format("sequence[name:%s]不存在,",
					sequenceName));
		}
		return getCreateSql(sequence, language);
	}

	private String getCreateSql(Sequence sequence, String language) {
//		ProcessorManager processorManager = SpringBeanContainer.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		SequenceSqlProcessor sqlProcessor = (SequenceSqlProcessor)processorManager.getProcessor(language, "sequence");
		return sqlProcessor.getCreateSql(sequence);
	}

	public String getDropSql(String sequenceName, String language) {
		Sequence sequence = getSequence(sequenceName);
		if (sequence == null) {
			throw new RuntimeException(String.format("sequence[name:%s]不存在,",
					sequenceName));
		}
		return getDropSql(sequence, language);
	}

	private String getDropSql(Sequence sequence, String language) {
//		ProcessorManager processorManager = SpringBeanContainer.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		SequenceSqlProcessor sqlProcessor = (SequenceSqlProcessor)processorManager.getProcessor(language, "sequence");
		return sqlProcessor.getDropSql(sequence);
	}

	public List<String> getCreateSql(String language) {
		List<String> sqls=new ArrayList<String>();
		for (Sequence  sequence : sequenceMap.values()) {
			sqls.add(getCreateSql(sequence, language));
		}
		return sqls;
	}

	public List<String> getDropSql(String language) {
		List<String> sqls=new ArrayList<String>();
		for (Sequence  sequence : sequenceMap.values()) {
			sqls.add(getDropSql(sequence, language));
		}
		return sqls;
	}

	public List<Sequence> getSequences(String language) {
        List<Sequence> sequences=new ArrayList<Sequence>();
        sequences.addAll(sequenceMap.values());
		return sequences;
	}

	public boolean checkSequenceExist(String language,Sequence sequence, Connection connection)
			throws SQLException {
//		ProcessorManager processorManager = SpringBeanContainer.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		SequenceSqlProcessor sqlProcessor = (SequenceSqlProcessor)processorManager.getProcessor(language, "sequence");
		return sqlProcessor.checkSequenceExist(sequence,connection);
	}

}
