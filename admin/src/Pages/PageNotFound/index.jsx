import { withHistory, withRouter, Redirect, Link } from 'react-router-dom';

const PageNotFound = withRouter(() => {
  return <div>
    <div>Page not found.</div>
    <Link to="/trang-chu">Go Homepage</Link>
  </div>
})

export { PageNotFound };