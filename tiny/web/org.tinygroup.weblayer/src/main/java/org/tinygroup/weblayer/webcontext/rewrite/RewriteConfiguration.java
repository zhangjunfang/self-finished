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
package org.tinygroup.weblayer.webcontext.rewrite;

import java.lang.reflect.Array;
import java.util.List;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteSubstitution.Parameter;
import org.tinygroup.xmlparser.node.XmlNode;

public class RewriteConfiguration extends AbstractConfiguration{
	
	private static final String CONDITION = "condition";
	private static final String PATTERN = "pattern";
	private static final String RULE = "rule";
	private static final String REWRITE_CONFIG="/application/rewrite";
	private RewriteRule[] rules;
	public String getApplicationNodePath() {
		return REWRITE_CONFIG;
	}

	public String getComponentConfigPath() {
		return null;
	}

	@Override
	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		super.config(applicationConfig, componentConfig);
		if(applicationConfig!=null){
			NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(applicationConfig);
			List<XmlNode> ruleNodes = nameFilter.findNodeList(RULE);
			if (!CollectionUtil.isEmpty(ruleNodes)) {
				rules = new RewriteRule[ruleNodes.size()];
				for (int i = 0; i < ruleNodes.size(); i++) {
					XmlNode ruleNode = ruleNodes.get(i);
					RewriteRule rule = new RewriteRule();
					rule.setPattern(ruleNode.getAttribute(PATTERN));
					resolverCondition(ruleNode, rule);
					resolverSubstitution(ruleNode, rule);
					resolverHandlers(ruleNode, rule);
					rules[i] = rule;
					try {
						rule.afterPropertiesSet();
					} catch (Exception e) {
						logger.errorMessage("initializingBean error", e);
						throw new RuntimeException("initializingBean error", e);
					}
				}

			}
		}
	}
	
	public RewriteRule[] getRules() {
		return rules;
	}

	private void resolverHandlers(XmlNode ruleNode,RewriteRule rule) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(ruleNode);
		List<XmlNode> subNodes = nameFilter.findNodeList("rewrite-handler");
		if (!CollectionUtil.isEmpty(subNodes)) {
			RewriteSubstitutionHandler[] handlers = (RewriteSubstitutionHandler[]) Array.newInstance(RewriteSubstitutionHandler.class, subNodes.size());
			for (int i = 0; i < subNodes.size(); i++) {
				XmlNode subNode=subNodes.get(i);
				String beanName=subNode.getAttribute("bean-name");
				RewriteSubstitutionHandler handler=BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean(beanName);
				handlers[i]=handler;
			}
			rule.setHandlers(handlers);
		}
	}
	
	private void resolverCondition(XmlNode ruleNode,RewriteRule rule) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(ruleNode);
		List<XmlNode> subNodes = nameFilter.findNodeList(CONDITION);
		if (!CollectionUtil.isEmpty(subNodes)) {
			RewriteCondition[] conditions = (RewriteCondition[]) Array.newInstance(RewriteCondition.class, subNodes.size());
			for (int i = 0; i < subNodes.size(); i++) {
				XmlNode subNode=subNodes.get(i);
				RewriteCondition condition=new RewriteCondition();
				condition.setTest(subNode.getAttribute("test"));
				condition.setFlags(subNode.getAttribute("flags"));
				condition.setPattern(subNode.getAttribute(PATTERN));
				conditions[i] = condition;
			}
			rule.setConditions(conditions);
		}
	}
	
	private  void resolverSubstitution(XmlNode ruleNode,RewriteRule rule){
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(ruleNode);
		XmlNode subNode = nameFilter.findNode("substitution");
		if(subNode!=null){
			RewriteSubstitution substitution=new RewriteSubstitution();
			substitution.setFlags(subNode.getAttribute("flags"));
			substitution.setUri(subNode.getAttribute("uri"));
			NameFilter<XmlNode> paramFilter = new NameFilter<XmlNode>(subNode);
			List<XmlNode> paramNodes = paramFilter.findNodeList("parameter");
			if(!CollectionUtil.isEmpty(paramNodes)){
				Parameter[] parameters = (Parameter[]) Array.newInstance(Parameter.class, paramNodes.size());
				for (int i = 0; i < parameters.length; i++) {
					XmlNode paramNode=paramNodes.get(i);
					Parameter parameter=new Parameter();
					parameter.setKey(paramNode.getAttribute("key"));
					parameter.setValue(paramNode.getAttribute("value"));//多个值以逗号分隔
					parameters[i]=parameter;
				}	
				substitution.setParameters(parameters);
			}
			rule.setSubstitution(substitution);
		}
	}
	
}
