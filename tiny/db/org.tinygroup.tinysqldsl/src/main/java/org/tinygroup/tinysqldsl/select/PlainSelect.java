/**
 * Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 * <p/>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/gpl.html
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.tinysqldsl.select;

import static org.tinygroup.tinysqldsl.util.DslUtil.getFormattedList;
import static org.tinygroup.tinysqldsl.util.DslUtil.getStringList;
import static org.tinygroup.tinysqldsl.util.DslUtil.orderByToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.SelectBody;
import org.tinygroup.tinysqldsl.base.Table;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.expression.OracleHierarchicalExpression;
import org.tinygroup.tinysqldsl.formitem.FromItem;
import org.tinygroup.tinysqldsl.selectitem.SelectItem;

/**
 * 普通的select对象
 * 
 * @author renhui
 * 
 */
public class PlainSelect implements SelectBody {
	private List<SelectItem> selectItems = new ArrayList<SelectItem>();
	private List<Table> intoTables = new ArrayList<Table>();
	private FromItem fromItem;
	private List<Join> joins;
	private Expression where;
	private List<Expression> groupByColumnReferences;
	private List<OrderByElement> orderByElements;
	private Expression having;
	private Limit limit;
	private Offset offset;
	private Fetch fetch;
	private OracleHierarchicalExpression oracleHierarchical = null;
	private boolean oracleSiblings = false;
	private boolean forUpdate = false;

	/**
	 * The {@link FromItem} in this query
	 * 
	 * @return the {@link FromItem}
	 */
	public FromItem getFromItem() {
		return fromItem;
	}

	public List<Table> getIntoTables() {
		return intoTables;
	}

	/**
	 * The {@link SelectItem}s in this query (for example the A,B,C in "SELECT
	 * A,B,C")
	 * 
	 * @return a list of {@link SelectItem}s
	 */
	public List<SelectItem> getSelectItems() {
		return selectItems;
	}

	public Expression getWhere() {
		return where;
	}

	public void setFromItem(FromItem item) {
		fromItem = item;
	}

	public void setIntoTables(List<Table> intoTables) {
		this.intoTables = intoTables;
	}

	public void setSelectItems(List<SelectItem> list) {
		selectItems = list;
	}

	public void addSelectItems(SelectItem... items) {
		if (selectItems == null) {
			selectItems = new ArrayList<SelectItem>();
		}
		Collections.addAll(selectItems, items);
	}

	public void addIntoTables(Table... tables) {
		if (intoTables == null) {
			intoTables = new ArrayList<Table>();
		}
		Collections.addAll(intoTables, tables);
	}

	public void setWhere(Expression where) {
		this.where = where;
	}

	/**
	 * The list of {@link Join}s
	 * 
	 * @return the list of {@link Join}s
	 */
	public List<Join> getJoins() {
		return joins;
	}

	public void setJoins(List<Join> list) {
		joins = list;
	}

	public void addJoins(Join... joinArray) {
		if (joins == null) {
			joins = new ArrayList<Join>();
		}
		Collections.addAll(joins, joinArray);
	}

	public List<OrderByElement> getOrderByElements() {
		return orderByElements;
	}

	public void setOrderByElements(List<OrderByElement> orderByElements) {
		this.orderByElements = orderByElements;
	}

	public void addOrderByElements(OrderByElement... orderBys) {
		if (orderByElements == null) {
			orderByElements = new ArrayList<OrderByElement>();
		}
		Collections.addAll(orderByElements, orderBys);
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	public Offset getOffset() {
		return offset;
	}

	public void setOffset(Offset offset) {
		this.offset = offset;
	}

	public Fetch getFetch() {
		return fetch;
	}

	public void setFetch(Fetch fetch) {
		this.fetch = fetch;
	}

	public Expression getHaving() {
		return having;
	}

	public void setHaving(Expression expression) {
		having = expression;
	}

	/**
	 * A list of {@link Expression}s of the GROUP BY clause. It is null in case
	 * there is no GROUP BY clause
	 * 
	 * @return a list of {@link Expression}s
	 */
	public List<Expression> getGroupByColumnReferences() {
		return groupByColumnReferences;
	}

	public void setGroupByColumnReferences(List<Expression> list) {
		groupByColumnReferences = list;
	}

	public void addGroupByExpressions(Expression... expressions) {
		if (groupByColumnReferences == null) {
			groupByColumnReferences = new ArrayList<Expression>();
		}
		Collections.addAll(groupByColumnReferences, expressions);
	}

	public OracleHierarchicalExpression getOracleHierarchical() {
		return oracleHierarchical;
	}

	public void setOracleHierarchical(
			OracleHierarchicalExpression oracleHierarchical) {
		this.oracleHierarchical = oracleHierarchical;
	}

	public boolean isOracleSiblings() {
		return oracleSiblings;
	}

	public void setOracleSiblings(boolean oracleSiblings) {
		this.oracleSiblings = oracleSiblings;
	}

	public boolean isForUpdate() {
		return forUpdate;
	}

	public void setForUpdate(boolean forUpdate) {
		this.forUpdate = forUpdate;
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(getStringList(selectItems));
		if (!CollectionUtil.isEmpty(intoTables)) {
			sql.append(" INTO ");
			for (Iterator<Table> iter = intoTables.iterator(); iter.hasNext();) {
				sql.append(iter.next().toString());
				if (iter.hasNext()) {
					sql.append(", ");
				}
			}
		}

		if (fromItem != null) {
			sql.append(" FROM ").append(fromItem);
			if (joins != null) {
				Iterator<Join> it = joins.iterator();
				while (it.hasNext()) {
					Join join = it.next();
					if (join.isSimple()) {
						sql.append(", ").append(join);
					} else {
						sql.append(" ").append(join);
					}
				}
			}
			// sql += getFormatedList(joins, "", false, false);
			if (where != null) {
				sql.append(" WHERE ").append(where);
			}
			if (oracleHierarchical != null) {
				sql.append(oracleHierarchical.toString());
			}
			sql.append(getFormattedList(groupByColumnReferences, "GROUP BY"));
			if (having != null) {
				sql.append(" HAVING ").append(having);
			}
			sql.append(orderByToString(oracleSiblings, orderByElements));
			if (limit != null) {
				sql.append(limit);
			}
			if (offset != null) {
				sql.append(offset);
			}
			if (fetch != null) {
				sql.append(fetch);
			}
			if (isForUpdate()) {
				sql.append(" FOR UPDATE");
			}
		}
		return sql.toString();
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		buffer.append("SELECT ");
		for (Iterator<SelectItem> iter = getSelectItems().iterator(); iter
				.hasNext();) {
			SelectItem selectItem = iter.next();
			selectItem.builder(builder);
			if (iter.hasNext()) {
				buffer.append(",");
			}
		}

		if (!CollectionUtil.isEmpty(getIntoTables())) {
			buffer.append(" INTO ");
			for (Iterator<Table> iter = getIntoTables().iterator(); iter
					.hasNext();) {
				iter.next().builder(builder);
				if (iter.hasNext()) {
					buffer.append(",");
				}
			}
		}

		if (getFromItem() != null) {
			buffer.append(" FROM ");
			getFromItem().builder(builder);
		}

		if (getJoins() != null) {
			for (Join join : getJoins()) {
				builder.deparseJoin(join);
			}
		}

		if (getWhere() != null) {
			buffer.append(" WHERE ");
			getWhere().builder(builder);
		}

		if (getOracleHierarchical() != null) {
			getOracleHierarchical().builder(builder);
		}

		if (getGroupByColumnReferences() != null) {
			buffer.append(" GROUP BY ");
			for (Iterator<Expression> iter = getGroupByColumnReferences()
					.iterator(); iter.hasNext();) {
				Expression columnReference = iter.next();
				columnReference.builder(builder);
				if (iter.hasNext()) {
					buffer.append(",");
				}
			}
		}

		if (getHaving() != null) {
			buffer.append(" HAVING ");
			getHaving().builder(builder);
		}

		if (getOrderByElements() != null) {
			builder.deparseOrderBy(isOracleSiblings(), getOrderByElements());
		}

		if (getLimit() != null) {
			builder.deparseLimit(getLimit());
		}
		if (getOffset() != null) {
			builder.deparseOffset(getOffset());
		}
		if (getFetch() != null) {
			builder.deparseFetch(getFetch());
		}
		if (isForUpdate()) {
			buffer.append(" FOR UPDATE");
		}

	}

}
