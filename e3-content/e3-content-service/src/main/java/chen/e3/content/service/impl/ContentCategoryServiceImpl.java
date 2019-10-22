package chen.e3.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import chen.e3.content.service.ContentCategoryService;
import chen.e3.entity.TbContentCategory;
import chen.e3.entity.TbContentCategoryExample;
import chen.e3.entity.TbItem;
import chen.e3.entity.TbItemExample;
import chen.e3.entity.TbContentCategoryExample.Criteria;
import chen.e3.mapper.TbContentCategoryMapper;
import chen.e3.utils.E3Result;
import chen.e3.utils.EasyUIResult;
import chen.e3.utils.TreeNode;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	public List<TreeNode> getContentCategoryList(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		
		List<TreeNode> result=new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			TreeNode treeNode=new TreeNode();
			treeNode.setId(tbContentCategory.getId());
			treeNode.setText(tbContentCategory.getName());
			treeNode.setState(tbContentCategory.getIsParent()?"closed":"open");
			
			result.add(treeNode);
		}
		return result;
	}

	public E3Result addContentCate(Long parentId, String name) {
		//创建一个TbContentCategory对象
		TbContentCategory tbContentCategory=new TbContentCategory();
		//补全对象属性
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setName(name);
		tbContentCategory.setStatus(1);//1正常2删除
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setIsParent(false);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		//插入数据库
		contentCategoryMapper.insert(tbContentCategory);
		//根据parentId查询父节点，判断父节点isparent属性是否是true，不是修改为true
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKeySelective(parent);
		}
		//返回结果
		return E3Result.ok(tbContentCategory);
	}

	public void updateContentCategory(Long id, String name) {
		//创建对象，为对象属性赋值，根据id修改数据库。
		TbContentCategory tbContentCategory=new TbContentCategory();
		tbContentCategory.setId(id);
		tbContentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
	}

	public E3Result deleteContentCategory(Long id) {
		//根据id查询到对应的对象，根据对象的isparent判断是否能删除该节点
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		if(tbContentCategory.getIsParent()){
			//是父节点，不允许删除
			return E3Result.build(500, "当前菜单有子菜单，不允许删除！！！");
		}else{
			//不是父节点，允许删除
			contentCategoryMapper.deleteByPrimaryKey(id);
			//根据当前对象的parentId查询父节点,判断是否还有子节点，没有修改为false
			TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andIdEqualTo(parent.getId());
			int i = contentCategoryMapper.countByExample(example);
			if(i==0){
				parent.setIsParent(false);
				contentCategoryMapper.updateByPrimaryKeySelective(parent);
			}
			return E3Result.ok();
		}
	}

	
}
