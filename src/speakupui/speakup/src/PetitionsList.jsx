import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const PetitionsList = ({ sort, searchQuery, filter, searchTrigger, userId, isUserPetitions, signFilter }) => {
  const [petitions, setPetitions] = useState([]);
  const [filteredPetitions, setFilteredPetitions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [unfilteredTotalElements, setUnfilteredTotalElements] = useState(0);
  const [signedPetitions, setSignedPetitions] = useState(new Set());

  const pageSize = 10;

  const fetchPetitions = useCallback(async () => {
    setLoading(true);
    setError(null);
    console.log('Fetching petitions for page:', page, 'sort:', sort, 'signFilter:', signFilter, 'searchQuery:', searchQuery);
    try {
      let response;
      if (isUserPetitions && userId && userId !== -1) {
        response = await axios.get(`http://localhost:8080/users/${userId}/petitions`, {
          headers: { 'Content-Type': 'application/json' }
        });

        let petitions = response.data;

        // Filter by search query (title) if provided
        if (searchQuery) {
          const q = searchQuery.toLowerCase();
          petitions = petitions.filter(p =>
            p.title.toLowerCase().includes(q)
          );
        }

        // Map to unified format first
        let mappedPetitions = petitions.map(petition => ({
          id: petition.id,
          title: petition.title,
          description: petition.description,
          signatures: petition.noOfSigners || petition.signatures || 0,
          creationDate: petition.creationDate,
          ownerUserName: petition.owner?.username || petition.ownerUserName || 'Unknown'
        }));

        // Then sort
        if (sort === 'most-signed') {
          mappedPetitions.sort((a, b) => b.signatures - a.signatures);
        } else if (sort === 'newest') {
          mappedPetitions.sort((a, b) => new Date(b.creationDate) - new Date(a.creationDate));
        }

        setPetitions(mappedPetitions);
        setTotalPages(1);
        setUnfilteredTotalElements(mappedPetitions.length);
      } else {
        // Public petitions: search + filters + pagination
        const params = {
          page,
          size: pageSize,
        };

        if (sort === 'newest') {
          params.sort = 'creationDate,desc';
        } else if (sort === 'most-signed') {
          params.sort = 'signatures,desc';
        }

        if (filter) {
          if (filter.category?.length) {
            params.category = filter.category.join(',');
          }
          if (filter.location?.length) {
            params.location = filter.location.join(',');
            params.scope = filter.locationScope; // If single-selection
          }
        }

        if (searchQuery) {
          params.search = searchQuery;
        }

        response = await axios.get('http://localhost:8080/petitions/searchbar', { params });

        setPetitions(response.data.content);
        setTotalPages(response.data.totalPages);
        setUnfilteredTotalElements(response.data.totalElements);
        console.log('Petitions fetched:', response.data.content.length, 'Total pages:', response.data.totalPages);
      }
    } catch (err) {
      console.error('Error fetching petitions:', err.response?.status, err.response?.data, err.message);
      setError('Failed to fetch petitions');
    } finally {
      setLoading(false);
    }
  }, [sort, searchQuery, filter, userId, isUserPetitions, signFilter, page]);

  useEffect(() => {
    fetchPetitions();
}, [fetchPetitions]);

  useEffect(() => {
    if (!isUserPetitions) {
      fetchPetitions();
    }
  }, [fetchPetitions, isUserPetitions]);

  useEffect(() => {
    if (isUserPetitions && petitions.length > 0) {
      if (searchQuery) {
        const q = searchQuery.toLowerCase();
        const filtered = petitions.filter(p => p.title.toLowerCase().includes(q));
        setFilteredPetitions(filtered);
        setUnfilteredTotalElements(filtered.length);
      } else {
        setFilteredPetitions(petitions);
        setUnfilteredTotalElements(petitions.length);
      }
    } else {
      let filtered = petitions;
      if (!isUserPetitions) {
        if (signFilter === 'signed') {
          filtered = petitions.filter(petition => signedPetitions.has(petition.id));
        } else if (signFilter === 'unsigned') {
          filtered = petitions.filter(petition => !signedPetitions.has(petition.id));
        }
      }
      setFilteredPetitions(filtered);
    }
  }, [petitions, searchQuery, signFilter, signedPetitions, isUserPetitions]);

  useEffect(() => {
    if (!userId || userId === -1 || petitions.length === 0) {
      setSignedPetitions(new Set());
      return;
    }

    const fetchSignedStatus = async () => {
      try {
        const signedStatusPromises = petitions.map(petition =>
          axios.get(`http://localhost:8080/petitions/${petition.id}/ispetitionSigned?userid=${userId}`)
            .then(response => ({ id: petition.id, isSigned: response.data }))
            .catch(err => {
              console.error(`Error checking signed status for petition ${petition.id}:`, err);
              return { id: petition.id, isSigned: false };
            })
        );

        const results = await Promise.all(signedStatusPromises);
        const signedIds = new Set(
          results.filter(result => result.isSigned).map(result => result.id)
        );
        setSignedPetitions(signedIds);
      } catch (err) {
        console.error('Error fetching signed statuses:', err);
      }
    };

    fetchSignedStatus();
  }, [petitions, userId]);

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      console.log('Changing page to:', newPage);
      setPage(newPage);
    }
  };

  if (loading) return <div>Loading petitions...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="petitions-list">
      <p>Total Petitions: {isUserPetitions ? filteredPetitions.length : unfilteredTotalElements}</p>
      {filteredPetitions.map(petition => (
        <Link
          to={`/petition/${petition.id}`}
          key={petition.id}
          style={{ textDecoration: 'none', color: 'inherit' }}
        >
          <div className="petition-card">
            {signedPetitions.has(petition.id) && (
              <span className="signed-message">Signed</span>
            )}
            <h3>{petition.title}</h3>
            <p>{petition.description}</p>
            <div className="petition-meta">
              <span><strong>By:</strong> {petition.ownerUserName || 'Unknown'}</span>
              <span><strong>Signatures:</strong> {petition.signatures}</span>
              <span><strong>Date:</strong> {new Date(petition.creationDate).toLocaleDateString()}</span>
            </div>
          </div>
        </Link>
      ))}

      {!isUserPetitions && (
        <div className="pagination-controls">
          <button
            onClick={() => handlePageChange(page - 1)}
            disabled={page === 0}
          >
            Previous
          </button>
          <span> Page {page + 1} of {totalPages} </span>
          <button
            onClick={() => handlePageChange(page + 1)}
            disabled={page + 1 >= totalPages}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default PetitionsList;
