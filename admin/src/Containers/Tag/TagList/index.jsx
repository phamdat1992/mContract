import React, { useState } from 'react';
import { IconEdit, IconAdd } from '@Components/Icon';
import TagForm from '../TagForm';
import { useDispatch, useSelector } from 'react-redux';
import TagService from '@Services/tag';
import { setTags } from '@Redux/Actions/Data';
import { setModal } from '@Redux/Actions/AlertModal';
const TagList = ({ onCheckTag, selectedTags }) => {
  const tags = useSelector(state => state.data.tags);
  const dispatch = useDispatch();

  const [isCreating, setIsCreating] = useState(false);
  const [isSubmiting, setIsSubmiting] = useState(false);
  const [editingTags, setEditingTags] = useState([]);
  const [search, setSearch] = useState('');

  function getTags() {
    TagService.list()
      .then(res => {
        const data = res.data.sort((a, b) => { return b.id - a.id });
        dispatch(setTags(data));
      })
      .catch(err => {
        dispatch(setModal({ isShow: true, message: err }));
      });
  }

  function onSearchChange(event) {
    setSearch(event.target.value);
  }

  function onStartEditTag(tag) {
    const newTag = Object.assign({}, tag);
    const newArray = [...editingTags, ...[newTag]];
    setEditingTags(newArray);
  }

  function onCancel(tag) {
    if (tag.id) {
      const newArray = editingTags.filter(t => t.id != tag.id);
      setEditingTags(newArray);
    } else {
      setIsCreating(false);
    }
  }

  function addTag(tag) {
    setIsSubmiting(true);
    TagService.add(tag)
      .then(res => {
        getTags();
        setIsCreating(false);
        setIsSubmiting(false);
      })
      .catch(err => {
        setIsCreating(false);
        setIsSubmiting(false);
        dispatch(setModal({ isShow: true, message: err }));
      })
  }

  function updateTag(tag) {
    TagService.edit({ id: tag.id, name: tag.name, colorCode: tag.colorCode })
      .then(res => {
        getTags();
        const newArray = editingTags.filter(item => item.id != tag.id);
        setEditingTags(newArray);
      })
      .catch(err => {
        dispatch(setModal({ isShow: true, message: err }));
      });
  }

  function onSave(tag) {
    if (tag.id) {
      updateTag(tag);
    } else {
      addTag(tag);
    }
  }

  return (
    <>
      <div className="row">
        <div className="col px-1">
          <input
            className="tag_search form-control form-control-sm mb-2"
            placeholder="Tìm kiếm thẻ"
            onChange={event => {
              onSearchChange(event);
            }}
          />
        </div>
      </div>
      {tags &&
        tags.map((tag, index) => {
          return (
            <React.Fragment key={index}>
              {tag.name.toUpperCase().indexOf(search.toUpperCase()) > -1 && (
                <>
                  {editingTags.length > 0 && editingTags.filter(item => item.id == tag.id).length > 0 ? (
                    <>
                      <TagForm key={index} index={index} tagData={tag} onCheckTag={onCheckTag} onCancel={onCancel} onSave={onSave} />
                    </>
                  ) : (
                    <>
                      <div className="row checktag_item" key={index}>
                        <div className="col-auto px-2 d-flex align-items-center">
                          <div className="checkbox">
                            <div className="custom-control custom-checkbox d-inline-block">
                              <input
                                key={`checkbox_${tag.id}`}
                                type="checkbox"
                                className="custom-control-input"
                                id={`checktag${tag.id}`}
                                defaultChecked={selectedTags.filter(id => id == tag.id).length > 0}
                                onChange={e => onCheckTag(e, tag.id)}
                              />
                              <label className="custom-control-label" htmlFor={`checktag${tag.id}`}></label>
                            </div>
                          </div>
                        </div>
                        <div className="col-auto p-0 d-flex align-items-center">
                          <svg className="flip_hor" viewBox="0 0 24 24" fill={tag.colorCode} width="22px" height="22px">
                            <path d="M0 0h24v24H0V0z" fill="none" />
                            <path d="M21.41 11.58l-9-9C12.05 2.22 11.55 2 11 2H4c-1.1 0-2 .9-2 2v7c0 .55.22 1.05.59 1.42l9 9c.36.36.86.58 1.41.58s1.05-.22 1.41-.59l7-7c.37-.36.59-.86.59-1.41s-.23-1.06-.59-1.42zM5.5 7C4.67 7 4 6.33 4 5.5S4.67 4 5.5 4 7 4.67 7 5.5 6.33 7 5.5 7z" />
                          </svg>
                        </div>
                        <div className="col pl-2 d-flex align-items-center show-tag-name" >
                          <span>{tag.name}</span>
                        </div>
                        <div className="col-auto pr-1">
                          <a className="tagedit_btn" href="javascript:void(0)" title="Sửa thẻ" onClick={() => onStartEditTag(tag)}>
                            <IconEdit />
                          </a>
                        </div>
                      </div>
                    </>
                  )}
                </>
              )}
            </React.Fragment>
          );
        })}
      {isCreating && <TagForm index={tags.length} tagData={null} onCheckTag={onCheckTag} onCancel={onCancel} isSubmiting={isSubmiting} onSave={onSave} />}
      {!isCreating &&
        <div className="row checktag_item quick_addtag" onClick={() => setIsCreating(true)}>
          <div className="col-auto px-2 d-flex align-items-center">
            <div className="checkbox" style={{ visibility: 'hidden', opacity: '0' }}>
              <div className="custom-control custom-checkbox d-inline-block">
                <input type="checkbox" className="custom-control-input" id="checktag1" />
                <label className="custom-control-label" htmlFor="checktag1"></label>
              </div>
            </div>
          </div>
          <div className="col-auto p-0 d-flex align-items-center">
            <svg width="1em" height="1em" viewBox="0 0 16 16" className="bi bi-plus" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path fillRule="evenodd" d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"></path>
            </svg>
          </div>
          <div className="col pl-2 d-flex align-items-center">
            <span>Thêm thẻ</span>
          </div>
          <div className="col-auto pr-1">
            <a className="tagedit_btn" href="javascript:void(0)" title="Sửa thẻ" style={{ visibility: 'hidden', opacity: '0' }}>
              <IconAdd />
            </a>
          </div>
        </div>
      }
    </>
  );
};
export default TagList;
