package chen.e3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chen.e3.entity.TbItemCat;
import chen.e3.entity.TbItemCatExample;
import chen.e3.entity.TbItemCatExample.Criteria;
import chen.e3.mapper.TbItemCatMapper;
import chen.e3.service.ItemCatService;
import chen.e3.utils.TreeNode;

@Service
public class ItemCatServiceImpl implements ItemCatService{

	@Autowired
	private TbItemCatMapper itemCatMapper;

	public List<TreeNode> getItemCatList(Long parentId) {
		//根据parentId查询数据库
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		
		List<TreeNode> resultList=new ArrayList<>();
		//循环list为resultList赋值
		for (TbItemCat tbItemCat : list) {
			TreeNode treeNode=new TreeNode();
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			resultList.add(treeNode);
		}
		return resultList;
	}

	
}
