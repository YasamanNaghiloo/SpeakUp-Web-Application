import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './petitionDetails.css';

const PetitionDetailView = ({ user }) => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [petition, setPetition] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [signError, setSignError] = useState(null);
  const [isSigned, setIsSigned] = useState(false);
  const [message, setMessage] = useState('');
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');

  const MAX_COMMENT_LENGTH = 2000;

  useEffect(() => {
    const fetchPetitionAndComments = async () => {
      try {
        const endpoint = user && user.id !== -1
          ? `http://localhost:8080/petitions/withuser/${id}?userId=${user.id}`
          : `http://localhost:8080/petitions/${id}`;
        const petitionRes = await axios.get(endpoint);
        setPetition(petitionRes.data);

        if (user && user.id !== -1) {
          const signedRes = await axios.get(`http://localhost:8080/petitions/${id}/ispetitionSigned?userid=${user.id}`);
          setIsSigned(signedRes.data);
        }

        const commentRes = await axios.get(`http://localhost:8080/petitions/${id}/comments`);
        setComments(commentRes.data);
      } catch (err) {
        console.error('Error fetching petition:', {
          message: err.message,
          response: err.response ? {
            status: err.response.status,
            data: err.response.data,
          } : null,
        });
        setError(`Failed to fetch petition: ${err.message}${err.response ? ` (Status: ${err.response.status})` : ''}`);
      } finally {
        setLoading(false);
      }
    };

    fetchPetitionAndComments();
  }, [id, user]);

  useEffect(() => {
    console.log('User:', user);
    console.log('Petition:', petition);
    if (user && petition) {
      console.log('Ownership check:', {
        userId: user.id,
        petitionOwnerId: petition.ownerId,
        creatorId: petition.creator?.id,
        isOwner: user.id !== -1 && (user.id === petition.ownerId || user.id === petition.creator?.id),
      });
    }
  }, [user, petition]);

  const sendEmail = () => {
    const responsibleEmail = petition?.responsible?.[0]?.email || 'default@example.com';
    const subject = encodeURIComponent(`Regarding Petition: ${petition?.title || 'Untitled Petition'}`);
    const body = encodeURIComponent(petition?.templateString || '');
    const mailtoLink = `mailto:${responsibleEmail}?subject=${subject}&body=${body}`;
    window.location.href = mailtoLink;
  };

  const signPetition = async () => {
    if (!user || user.id === -1) {
      setSignError('You must be logged in to sign a petition.');
      return;
    }

    try {
      await axios.post(`http://localhost:8080/petitions/${id}/sign`, null, {
        params: { userid: user.id },
      });
      setMessage('Petition signed!');
      setIsSigned(true);
      setSignError(null);
      const endpoint = user && user.id !== -1
        ? `http://localhost:8080/petitions/withuser/${id}?userId=${user.id}`
        : `http://localhost:8080/petitions/${id}`;
      const response = await axios.get(endpoint);
      setPetition(response.data);
    } catch (err) {
      setSignError(err.response?.data || 'Failed to sign petition.');
      setMessage('');
    }
  };

  const handleRevoke = async () => {
    if (!user || user.id === -1) {
      setSignError('You must be logged in to revoke a signature.');
      return;
    }

    try {
      await axios.delete(`http://localhost:8080/petitions/${id}/revoke`, {
        params: { userid: user.id },
      });
      setMessage('Revoked signature');
      setIsSigned(false);
      setSignError(null);
      const endpoint = user && user.id !== -1
        ? `http://localhost:8080/petitions/withuser/${id}?userId=${user.id}`
        : `http://localhost:8080/petitions/${id}`;
      const response = await axios.get(endpoint);
      setPetition(response.data);
    } catch (err) {
      setSignError(err.response?.data || 'Failed to revoke signature.');
      setMessage('');
    }
  };

  const handleDelete = async () => {
    if (!user || user.id === -1) {
      setSignError('You must be logged in to delete a petition.');
      return;
    }

    if (!window.confirm('Are you sure you want to delete this petition? This action cannot be undone.')) {
      return;
    }

    try {
      await axios.delete(`http://localhost:8080/petitions/${id}`, {
        params: { userid: user.id },
      });
      setMessage('Petition deleted!');
      setSignError(null);
      console.log('Post-deletion user state:', user);
      setTimeout(() => {
        try {
          navigate('/');
        } catch (navError) {
          console.error('Navigation error:', navError);
        }
      }, 2000);
    } catch (err) {
      setSignError(err.response?.data || 'Failed to delete petition.');
      setMessage('');
      console.error('Delete error:', err.response?.data, err);
    }
  };

  const handleAddComment = async () => {
    if (!newComment.trim()) return;

    try {
      await axios.post(`http://localhost:8080/petitions/${id}/comments`, {
        comment: newComment,
        petitionId: Number(id),
        userId: user.id,
      });

      const commentRes = await axios.get(`http://localhost:8080/petitions/${id}/comments`);
      setComments(commentRes.data);
      setNewComment('');
    } catch (err) {
      console.error('Failed to post comment', err);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) {
    if (error.includes('404')) {
      return <div>Petition not found. Please check the ID or try another petition.</div>;
    } else if (error.includes('500')) {
      return <div>Server error. Please try again later.</div>;
    }
    return <div>{error}</div>;
  }
  if (!petition) return <div>No petition found</div>;

  const {
    title = 'N/A',
    description = 'N/A',
    goal = 'N/A',
    creationDate = 'N/A',
    endDate = 'N/A',
    category = 'N/A',
    locationScope = 'N/A',
    locationCountry = 'N/A',
    locationCity = 'N/A',
    creator = {},
    responsible = [],
    noOfSigners = 0,
    ownerId
  } = petition;

  const isOwner = user && user.id !== -1 && (user.id === ownerId || user.id === petition.creator?.id);

  // Add active/expired status
  const isExpired = new Date(endDate) < new Date();
  const statusText = isExpired ? 'Expired' : 'Active';

  return (
    <div className="petition-detail-page">
      <div className="main-content">
        <h1 className="subtitle">
          {title === 'Test' ? 'Petition Details' : title}
        </h1>
        <div className="petition-cards">
          <p><strong>Description:</strong> {description}</p>
          <p><strong>Goal:</strong> {goal} signatures</p>
          <p><strong>Current Signers:</strong> {noOfSigners}</p>
          <p><strong>Created On:</strong> {new Date(creationDate).toLocaleString()}</p>
          <p><strong>Ends On:</strong> {new Date(endDate).toLocaleDateString()}</p>
          <p><strong>Category:</strong> {category}</p>
          <p><strong>Scope:</strong> {locationScope}</p>
          <p><strong>Country:</strong> {locationCountry}</p>
          <p><strong>City:</strong> {locationCity}</p>
          <p>
          </p>
          <p><strong>Creator:</strong> {creator?.name || 'N/A'}</p>
          <div>
            <strong>Responsible Parties:</strong>
            <ul>
              {responsible?.map((r, idx) => (
                <li key={idx}>{r.name}</li>
              ))}
            </ul>
            <strong>Status:</strong>{' '}
            <span className={isExpired ? 'status-text expired' : 'status-text active'}>
              {statusText}
            </span>
          </div>
          {message && <p className="success">{message}</p>}
          {signError && <p className="error">{signError}</p>}
          <div className="button-rows">
            {user && user.id !== -1 ? (
              <>
                {isSigned ? (
                  <button className="primary1" onClick={handleRevoke}>Revoke Signature</button>
                ) : (
                  <button className="primary1" onClick={signPetition}>Sign Petition</button>
                )}
                <button className="secondary2" onClick={sendEmail}>Contact</button>
                {isOwner && (
                  <button className="delete-button" onClick={handleDelete}>Delete Petition</button>
                )}
              </>
            ) : (
              <p className="guest-message">Log in to sign a petition or contact responsible persons.</p>
            )}
          </div>

          <div className="comments-section">
            <h2>Comments</h2>
            <ul className="comment-list">
              {comments.length === 0 && <li>No comments yet.</li>}
              {comments.map((comment, index) => (
                <li key={index} className="comment-item">
                  <p>
                    <strong>{comment.userName}:</strong> {comment.comment}
                  </p>
                </li>
              ))}
            </ul>

            {user && user.id !== -1 ? (
              <div className="comment-form">
                <textarea
                  placeholder="Add a comment..."
                  value={newComment}
                  onChange={(e) => {
                    const text = e.target.value;
                    if (text.length <= MAX_COMMENT_LENGTH) {
                      setNewComment(text);
                    }
                  }}
                  maxLength={MAX_COMMENT_LENGTH}
                />
                <div className="char-count">{2000 - newComment.length} characters left</div>
                <button className="primary1" onClick={handleAddComment}>Post Comment</button>
              </div>
            ) : (
              <p className="guest-message">Log in to comment.</p>
            )}
          </div>

        </div>
      </div>
    </div>
  );
};

export default PetitionDetailView;
