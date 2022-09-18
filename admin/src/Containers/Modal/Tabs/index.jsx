import { useSelector } from 'react-redux';

TabsModal.defaultProps = {
  stylePeople: 'px-2 row py-2',
  styleComment: 'row px-2 pt-2',
};
function TabsModal(props) {
  const { newComment, focusComment } = useSelector(state => state.detailContract);

  return (
    <>
      <div className="modal fade" id="peopleModal" tabIndex="-1" aria-labelledby="ModalLabel" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered modal-dialog-scrollable">
          <div className="modal-content">
            <div className="modal-header"></div>
            <div className="modal-body info_tabs"></div>
          </div>
        </div>
      </div>

      <div className="modal fade" id="commentModal" tabIndex="-1" aria-labelledby="ModalLabel" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered modal-dialog-scrollable">
          <div className="modal-content">
            <div className="modal-header"></div>
            <div className="modal-body info_tabs">
              <div className="pb-2"></div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default TabsModal;
