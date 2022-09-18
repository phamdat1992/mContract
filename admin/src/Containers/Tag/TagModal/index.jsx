import { useEffect, useState } from 'react';
import { Modal } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import ContractService from '@Services/contract';
import TagList from '../TagList';
import SimpleBar from "simplebar-react";
import styles from "./index.module.scss";
import { setContractTags } from '@Redux/Actions/DetailContract';
import { setTags } from '@Redux/Actions/Data';
import TagService from '@Services/tag';
import { setModal } from '@Redux/Actions/AlertModal';
import { checkDevice } from '../../../Utils/helpers';
import { useLocation } from 'react-router-dom';
function generateSelectedTags(contracts, tags) {
  const array = [];
  if (contracts) {
    contracts.forEach(contract => {
      if (contract.tagDtos) {
        contract.tagDtos.forEach(tag => {
          if (array.filter(c => c.id == tag.id).length == 0 && tags.filter(t => t.id == tag.id).length > 0) {
            array.push(tag);
          }
        });
      }
    });
  }
  return array.map(c => c.id);
}

function TagModal({ show = false, onHide = null, contracts = [], getContracts }) {
  const dispatch = useDispatch();
  const user = useSelector(state => state.auth.user);
  const { contract } = useSelector(state => state.detailContract);
  const tags = useSelector(state => state.data.tags);
  const location = useLocation();
  const [showModal, setShowModal] = useState(false);
  const [selectedTags, setSelectedTags] = useState([]);
  function getTitle() {
    let titleModal = 'Quản lý thẻ';
    if (contracts && contracts.length > 0) {
      if (contracts.length > 1) {
        titleModal = 'Gắn thẻ';
      } else {
        titleModal = `Gắn thẻ: ${contracts[0].title}`;
      }
    }
    return titleModal;
  }
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

  useEffect(() => {
    if (!tags || tags.length == 0) {
      if (user) {
        if (contract && contract.id) {
          getTags();
        }
      }
    }
  }, []);

  useEffect(() => {
    setShowModal(show);
    if (show && tags && tags.length > 0) {
      setSelectedTags(generateSelectedTags(contracts, tags));
    } else {
      setSelectedTags([]);
    }
  }, [show]);

  function onCheckTag(e, id) {
    if (e.target.checked) {
      selectedTags.push(id);
      setSelectedTags(selectedTags);
    } else {
      const newSelectedTags = selectedTags.filter(a => a != id);
      setSelectedTags(newSelectedTags);
    }
  }

  function onAddTag(data) {
    ContractService.attachTag(data)
      .then(res => {
        if (getContracts) {
          getContracts();
          getTags();
        } else {
          const tagsArr = tags.filter(item => {
            if (data.tagListId.filter(id => item.id == id).length > 0) {
              return item;
            }
          });
          dispatch(setContractTags(tagsArr));
        }
        onHide(true);
      })
      .catch(err => {
        dispatch(setModal({ isShow: true, message: err }));
      });
  }

  function onRemoveTag(data) {
    ContractService.removeTag(data)
      .then(res => {
        if (getContracts) {
          getContracts();
          getTags();
        } else {
          const tagsArr = contracts[0].tagDtos.filter(item => {
            if (data.tagListId.filter(id => item.id == id).length == 0) {
              return item;
            }
          });
          dispatch(setContractTags(tagsArr));
        }
        onHide(true);
      })
      .catch(err => {
        dispatch(setModal({ isShow: true, message: err }));
      });
  }

  function assignTags() {
    const generalTags = generateSelectedTags(contracts, tags);
    if (generalTags.length > 0) {
      const oldTag = [];
      generalTags.forEach(tagId => {
        if ((selectedTags.filter(i => i == tagId)).length == 0)
          oldTag.push(tagId)
      });
      const oldData = {
        tagListId: oldTag,
        contractListId: contracts.map(item => item.id),
      };
      if (oldTag.length > 0) {
        onRemoveTag(oldData)
      }
      const data = {
        tagListId: selectedTags,
        contractListId: contracts.map(item => item.id),
      };

      if (selectedTags.length > 0) {
        onAddTag(data);
      }
    } else {
      if (selectedTags.length == 0) {
        onHide(true);
      } else {
        const data = {
          tagListId: selectedTags,
          contractListId: contracts.map(item => item.id),
        };
        onAddTag(data);
      }
    }
  }

  return (
    <>
      <Modal show={showModal} onHide={onHide} dialogClassName="modal_site modal-dialog-centered modal-dialog-scrollable">
        <Modal.Header className="d-block text-center">
          <Modal.Title className=" h5 si_name" style={{ color: '#0d7aca', fontWeight: 400 }}>
            {getTitle()}
          </Modal.Title>
        </Modal.Header>
        <SimpleBar className={`${styles.max_height_tags}`}>
          <Modal.Body className="body-modal-tags">
            <div className="container-fluid">
              <TagList onCheckTag={onCheckTag} contracts={contracts} selectedTags={selectedTags} />
            </div>
          </Modal.Body>
        </SimpleBar>

        {/* {!checkDevice() ? <SimpleBar className={`${styles.max_height_tags}`}>
          <Modal.Body className="body-modal-tags">
            <div className="container-fluid">
              <TagList onCheckTag={onCheckTag} contracts={contracts} selectedTags={selectedTags} />
            </div>
          </Modal.Body>
        </SimpleBar> :
          <div className={`${styles.max_height_tags}`}>
            <Modal.Body className="body-modal-tags">
              <div className="container-fluid">
                <TagList onCheckTag={onCheckTag} contracts={contracts} selectedTags={selectedTags} />
              </div>
            </Modal.Body>
          </div>} */}

        <Modal.Footer className="d-block new_contract_nav d-block text-center text-uppercase">
          <button type="button" className="btn btn_outline_main mx-2 mx-sm-3" onClick={onHide}>
            {(contracts && contracts.length > 0) ? 'HỦY BỎ' : 'HOÀN TẤT'}
          </button>
          {contracts.length == 0 ? (
            <></>
          ) : (
            <button type="submit" className="btn btn_new mx-2 mx-sm-3" onClick={() => assignTags()}>
              LƯU LẠI
            </button>
          )}
        </Modal.Footer>
        {/* </>} */}
      </Modal>
    </>
  );
}

export default TagModal;
